import sys
import os
import re
import subprocess
import time
import urllib.request
import urllib.error
import html

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

def fetch_boj_data(prob_id):
    url = f"https://www.acmicpc.net/problem/{prob_id}"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
    }
    
    try:
        req = urllib.request.Request(url, headers=headers)
        with urllib.request.urlopen(req) as response:
            content = response.read().decode('utf-8')
            
        # 시간 제한 추출
        # <table id="problem-info"> ... <td>1 초</td>
        time_limit_match = re.search(r'시간 제한[\s\S]*?<td>([\d.]+)\s*초', content)
        time_limit = float(time_limit_match.group(1)) if time_limit_match else 2.0
        
        # 메모리 제한 추출
        mem_limit_match = re.search(r'메모리 제한[\s\S]*?<td>(\d+)\s*MB', content)
        mem_limit = int(mem_limit_match.group(1)) if mem_limit_match else 128
        
        # 예제 입력/출력 추출
        # <pre id="sample-input-1" ...>...</pre>
        inputs = re.findall(r'<pre[^>]*id="sample-input-\d+"[^>]*>([\s\S]*?)</pre>', content)
        outputs = re.findall(r'<pre[^>]*id="sample-output-\d+"[^>]*>([\s\S]*?)</pre>', content)
        
        test_cases = []
        for i, o in zip(inputs, outputs):
            # HTML 엔티티 변환 (&lt; -> < 등) 및 공백 정리
            clean_i = html.unescape(i).strip().replace('\r\n', '\n')
            clean_o = html.unescape(o).strip().replace('\r\n', '\n')
            test_cases.append((clean_i, clean_o))
            
        return time_limit, mem_limit, test_cases
        
    except urllib.error.HTTPError as e:
        print(f"Error fetching from BOJ: HTTP {e.code}")
    except Exception as e:
        print(f"Error fetching from BOJ: {str(e)}")
        
    return None, None, []

def run_judge(source_path):
    if not os.path.exists(source_path):
        print(f"Error: File not found: {source_path}")
        return

    # 문제 파일 찾기
    source_dir = os.path.dirname(os.path.abspath(source_path))
    parent_dir = os.path.dirname(source_dir)
    filename = os.path.basename(source_path)
    prob_id_match = re.search(r'(\d+)', filename)
    if not prob_id_match:
        print(f"Error: Could not find problem ID in filename: {filename}")
        return
    prob_id = prob_id_match.group(1)
    
    import unicodedata
    def normalize(text):
        return unicodedata.normalize('NFC', text)

    print(f"Fetching problem data from Baekjoon for ID {prob_id}...")
    t_limit, m_limit, tests = fetch_boj_data(prob_id)
    
    if not tests:
        print(f"Error: Could not fetch data for problem {prob_id} from Baekjoon.")
        return
    
    print(f"Successfully fetched problem data from Baekjoon.")
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
        full_class_name = class_name
        
        # 패키지 선언 감지
        with open(source_path, 'r', encoding='utf-8') as f:
            java_content = f.read()
            pkg_match = re.search(r'package\s+([\w.]+);', java_content)
            if pkg_match:
                full_class_name = f"{pkg_match.group(1)}.{class_name}"
        
        # 현재 작업 디렉토리를 클래스패스에 추가
        cmd = ["java", "-cp", f".{os.pathsep}{source_dir}", full_class_name]
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
