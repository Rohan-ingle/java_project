from cryptography.fernet import Fernet

def generate_key():
    key = Fernet.generate_key()
    return key

with open('key.txt','wb') as f:
    f.write(generate_key())