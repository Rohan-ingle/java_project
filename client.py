import socket

def send_file(server_ip, server_port, file_name):
    # Create a TCP/IP socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        # Connect the socket to the server's IP address and port
        s.connect((server_ip, server_port))
        print("Connected to server.")

        # Open the file to send
        with open(file_name, 'rb') as f:
            print("Sending file...")
            # Read the file in chunks and send over the socket
            data = f.read(1024)
            while data:
                s.sendall(data)
                data = f.read(1024)
            print("File sent successfully.")

# Usage
if __name__ == "__main__":
    server_ip = 'server_ip_address'  # Change this to the IP address of the server
    server_port = 12345  # Change this to the port the server is listening on
    file_name = 'file_to_send.txt'  # Change this to the name of the file you want to send
    send_file(server_ip, server_port, file_name)