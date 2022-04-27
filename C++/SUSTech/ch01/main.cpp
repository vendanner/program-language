#include <iostream>

#include "mul.h"

using namespace std;

int main()
{
  int a, b;
  int result;

  cout << "输入两个数据: ";
  cin >> a;
  cin >> b;

  result = mul(a, b);

  cout << "result = " << result << endl;

  return 0;
}