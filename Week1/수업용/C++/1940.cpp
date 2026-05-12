#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

int main()
{
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int n, m;
    cin >> n >> m;
    vector<int> a(n);
    for (int i = 0; i < a.size(); i++)
    {
        cin >> a[i];
    }

    sort(a.begin(), a.end());

    int count = 0;
    int i = 0;
    int j = n - 1;

    while (i < j)
    {
        if (a[i] + a[j] < m)
        {
            i++;
        }
        else if (a[i] + a[j] > m)
        {
            j--;
        }
        else
        {
            count++;
            i++;
            j--;
        }
    }

    cout << count << endl;

    return 0;
}