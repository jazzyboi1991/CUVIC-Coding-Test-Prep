import sys
import os
import re
import subprocess
import time

def print_result(case_num, status, exec_time, memory, expected, actual):
    color = "\033[92m" if status == "PASS" else "\033[91m"
    reset = "\033[0m"
    print(f"CASE #{case_num}: {color}{status}{reset} | Time: {exec_time:.3f}s | Memory: {memory:.2f}MB")
    if status != "PASS":
        print(f"  [Expected]: {repr(expected)[:100]}")
        print(f"  [Actual]:   {repr(actual)[:100]}")

def parse_md(md_path):
    with open(md_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 시간 및 메모리 제한 추출
    time_limit_match = re.search(r'시간 제한:\s*([\d.]+)\s*초', content)
    mem_limit_match = re.search(r'메모리 제한:\s*(\d+)\s*MB', content)
    
    time_limit = float(time_limit_match.group(1)) if time_limit_match else 2.0
    mem_limit = int(mem_limit_match.group(1)) if mem_limit_match else 128
    
    # 테스트 케이스 추출
    inputs = re.findall(r'예제 입력 \d+[\s\S]*?```([\s\S]*?)```', content)
    outputs = re.findall(r'예제 출력 \d+[\s\S]*?```([\s\S]*?)```', content)
    
    test_cases = []
    for i, o in zip(inputs, outputs):
        test_cases.append((i.strip(), o.strip()))
        
    return time_limit, mem_limit, test_cases

def run_judge(source_path):
    if not os.path.exists(source_path):
        print(f"Error: File not found: {source_path}")
        return

    # 문제 파일 찾기
    source_dir = os.path.dirname(os.path.abspath(source_path))
    parent_dir = os.path.dirname(source_dir)
    filename = os.path.basename(source_path)
    prob_id = re.match(r'^(\d+)', filename).group(1)
    
    import unicodedata
    def normalize(text):
        return unicodedata.normalize('NFC', text)

    md_file = None
    prob_id = normalize(prob_id)
    
    for f in os.listdir(parent_dir):
        norm_f = normalize(f)
        if norm_f.startswith(prob_id) and norm_f.endswith("_문제.md"):
            md_file = os.path.join(parent_dir, f)
            break
    
    if not md_file:
        for f in os.listdir(source_dir):
            norm_f = normalize(f)
            if norm_f.startswith(prob_id) and norm_f.endswith("_문제.md"):
                md_file = os.path.join(source_dir, f)
                break

    if not md_file:
        print(f"Error: Problem MD file for ID {prob_id} not found in {parent_dir} or {source_dir}")
        return

    print(f"Found Problem: {os.path.basename(md_file)}")
    t_limit, m_limit, tests = parse_md(md_file)
    print(f"Constraints: Time {t_limit}s, Memory {m_limit}MB")
    print("-" * 50)

    # 컴파일 및 실행 설정
    ext = os.path.splitext(source_path)[1]
    cmd = []
    cleanup = []

    if ext == ".py":
        cmd = ["python3", source_path]
    elif ext == ".cpp":
        executable = os.path.join(source_dir, "sol_bin")
        compile_cmd = ["g++", "-O2", source_path, "-o", executable]
        print(f"Compiling C++...")
        if subprocess.run(compile_cmd).returncode != 0:
            print("Compilation Failed")
            return
        cmd = [executable]
        cleanup.append(executable)
    elif ext == ".java":
        print(f"Compiling Java...")
        if subprocess.run(["javac", source_path]).returncode != 0:
            print("Compilation Failed")
            return
        class_name = os.path.splitext(filename)[0]
        cmd = ["java", "-cp", source_dir, class_name]
        cleanup.append(os.path.join(source_dir, f"{class_name}.class"))
    else:
        print(f"Unsupported extension: {ext}")
        return

    # 테스트 실행
    pass_count = 0
    for i, (inp, exp) in enumerate(tests, 1):
        # macOS /usr/bin/time -l 사용
        time_cmd = ["/usr/bin/time", "-l"] + cmd
        
        try:
            process = subprocess.Popen(
                time_cmd,
                stdin=subprocess.PIPE,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )
            
            stdout, stderr = process.communicate(input=inp, timeout=t_limit + 1)
            
            # 성능 데이터 추출 (stderr에서)
            # real time
            exec_time_match = re.search(r'([\d.]+)\s+real', stderr)
            # max RSS (bytes)
            mem_match = re.search(r'(\d+)\s+maximum resident set size', stderr)
            
            exec_time = float(exec_time_match.group(1)) if exec_time_match else 0.0
            memory_bytes = int(mem_match.group(1)) if mem_match else 0
            memory_mb = memory_bytes / (1024 * 1024)
            
            actual = stdout.strip()
            
            def is_match(exp, act):
                if exp == act: return True
                try:
                    # 부동 소수점 비교 시도 (절대/상대 오차 1e-4 허용)
                    e_val = float(exp)
                    a_val = float(act)
                    return abs(e_val - a_val) < 1e-4 or abs((e_val - a_val) / max(abs(e_val), 1e-9)) < 1e-4
                except:
                    return False

            status = "PASS"
            if not is_match(exp, actual):
                status = "WRONG ANSWER"
            elif exec_time > t_limit:
                status = "TIME LIMIT EXCEEDED"
            elif memory_mb > m_limit:
                status = "MEMORY LIMIT EXCEEDED"
            
            if status == "PASS":
                pass_count += 1
            
            print_result(i, status, exec_time, memory_mb, exp, actual)
            
        except subprocess.TimeoutExpired:
            print_result(i, "TIME LIMIT EXCEEDED", t_limit + 1, 0, exp, "Timeout")
        except Exception as e:
            print(f"CASE #{i}: ERROR - {str(e)}")

    print("-" * 50)
    print(f"Summary: {pass_count}/{len(tests)} cases passed.")

    # 정리
    for f in cleanup:
        if os.path.exists(f):
            os.remove(f)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 judge.py <source_file>")
    else:
        run_judge(sys.argv[1])
