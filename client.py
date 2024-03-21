import socket
import os
import time
from cryptography.fernet import Fernet

def send_file(server_ip, server_port, file_path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        with open('key.txt','rb') as key_file:
            key = key_file.read()
            print(key)

        while True:
            try:
                s.connect((server_ip, server_port))
                print("Connected to server.")
                break  # If connection is successful, exit the loop
            except ConnectionRefusedError:
                pass  # If connection is refused, try again
            except KeyboardInterrupt:
                print("User interrupted the connection attempt.")
                break  # Exit the loop if user interrupts
        

        fernet = Fernet(key)
        # Send file name
        file_name_bytes = (os.path.basename(file_path)).encode('utf-8')
        s.sendall(file_name_bytes + b'\n')  # Send file name with '\n' as delimiter

        with open(file_path, 'rb') as f:
            file_data = f.read()
            encrypted_data = fernet.encrypt(file_data)  # Encrypt the file data

        s.sendall(encrypted_data)  # Send the encrypted data
        print("Encrypted file sent successfully.")

# Usage
if __name__ == "__main__":
    # server_ip = '127.0.0.1'
    # server_port = 5050
    server_ip = '127.0.0.1'
    server_port = 5050
    file_path = r"D:\wallpapers\anime-night-stars-sky-clouds-scenery-digital-art-4k-wallpaper-uhdpaper.com-772@0@i.jpg"
    send_file(server_ip, server_port, file_path)