#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

int main()
{
    int N;
    cin >> N;

    vector<int> scores(N);
    int max_score = 0;
    double sum = 0;

    for (int i = 0; i < N; i++)
    {
        cin >> scores[i];
    }

    max_score = *max_element(scores.begin(), scores.end());

    for (int i = 0; i < N; i++)
    {
        sum += scores[i];
    }

    cout << (sum / max_score * 100.0) / N << "\n";

    return 0;
}