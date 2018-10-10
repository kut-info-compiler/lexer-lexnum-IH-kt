# lexer-lexnum-template

数値を字句解析するプログラムのテンプレート

# 使い方

プログラム(`Lexer.java`)中の `TODO` コメントを実装してください

# 字句の定義

```
0                           -> 整数 (例: 0)
[1-9][0-9]*                 -> 整数 (例: 100)
0[xX][0-9a-fA-F]+           -> 整数 (例: 0xabc)
[0-9]*[a-fA-F][0-9a-fA-F]*  -> 整数 (例: 0123456789a)
[1-9][0-9]*\.[0-9]*         -> 小数 (例: 10.3)
0\.[0-9]*                   -> 小数 (例: 0.12)
\.[0-9]+                    -> 小数 (例: .12)
```

# テスト

  * `test.sh` で差分がないことを確認する．
  * `test.in` に入力と正解の出力を書く．
