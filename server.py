import socket
from cryptography.fernet import Fernet

def receive_file(server_ip, server_port, key):
    
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((server_ip, server_port))
        s.listen(1)
        print("Waiting for connection...")
        
        conn, addr = s.accept()
        print("Connected to:", addr)

        with open('key.txt','rb') as key_file:
            key = key_file.read()
            print(key)

        fernet = Fernet(key)
        
        file_name_data = b''
        while True:  # Receive file name
            chunk = conn.recv(1024)
            if b'\n' in chunk:
                file_name_data += chunk
                break
            file_name_data += chunk
        file_name = file_name_data.strip().decode('utf-8')

        encrypted_data = b''
        while True:  # Receive encrypted data
            chunk = conn.recv(1024)
            if not chunk:
                break
            encrypted_data += chunk

        decrypted_data = fernet.decrypt(encrypted_data)  # Decrypt the data

        with open(f'./received_files/encrypted_{file_name}', 'wb') as f:
            f.write(encrypted_data)

        with open(f'./received_files/{file_name}', 'wb') as f:
            f.write(decrypted_data)  # Write the decrypted data to file
        print("File received and decrypted successfully.")

if __name__ == "__main__":
    server_ip = '127.0.0.1'
    server_port = 5050
    
    # Use the same key as in client.py, should be securely shared/stored
    key = b'your_fernet_key_here'
    
    receive_file(server_ip, server_port, key)
