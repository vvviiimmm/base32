# base32
Base32 encoding work similarly to [Base64](https://en.wikipedia.org/wiki/Base64) but uses 5 bits patterns for encoded character index number
(instead of 6 bits in Base64) which allows us to encode any sequence of bytes using only 26 lowercase ['a'..'z']
and 6 uppercase ['A'..'G'] english alphaabet characters. Encoding and decoding is done without [padding](https://en.wikipedia.org/wiki/Base64#Padding).
