from Cryptography import generate_key

with open('key.txt','wb') as f:
    f.write(generate_key())