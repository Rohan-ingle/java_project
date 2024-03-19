import socket

def receive_file(server_ip, server_port, file_name):
    # Create a TCP/IP socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        # Bind the socket to the server address and port
        s.bind((server_ip, server_port))
        # Listen for incoming connections
        s.listen(1)
        print("Waiting for connection...")
        # Accept a connection
        conn, addr = s.accept()
        print("Connected to:", addr)

        with conn:
            # Open the file to write received data
            with open(file_name, 'wb') as f:
                print("Receiving file...")
                # Receive the file data in chunks and write to file
                while True:
                    data = conn.recv(1024)
                    if not data:
                        break
                    f.write(data)
                print("File received successfully.")

# Usage
if __name__ == "__main__":
    server_ip = 'your_server_ip'  # Change this to your server IP
    server_port = 12345  # Change this to your desired port
    file_name = 'received_file.txt'  # Change this to the name you want to save the received file as
    receive_file(server_ip, server_port, file_name)
