# Xcipher
Xcipher is a private key encryption program written in Java. It works on the simple concept that Java will generate the same set of random numbers 
if you seed the number generator. The program will read the file and compare each byte with a mask generated from the password. The 
output of the xor comparison will be stored in the encrypted file byte by byte. The trick is when you "xor" a byte from the file with the mask, which is
also a byte, it will create an encrypted byte. If you "xor" that encrypted byte with the same mask it will generate the original byte.

The program does encrypt and decrypt like designed, but I have planned changes that will make it more secure.

![Xcipher](https://github.com/TAllenLucas/xcipher/blob/Master/screenshot.JPG?raw=true)
