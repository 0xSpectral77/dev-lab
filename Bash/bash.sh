#!/bin/bash

IP=$1
THREADS=$2
declare -A SERVICIO=(
    [1]="TCPMUX"
    [5]="RJE"
    [7]="ECHO"
    [9]="DISCARD"
    [11]="SYSTAT"
    [13]="DAYTIME"
    [15]="NETSTAT"
    [17]="QOTD"
    [18]="MSP"
    [19]="CHARGEN"
    [20]="FTP-DATA"
    [21]="FTP"
    [22]="SSH"
    [23]="TELNET"
    [24]="PRIV-MAIL"
    [25]="SMTP"
    [37]="TIME"
    [42]="NAMESERVER"
    [43]="WHOIS"
    [49]="TACACS"
    [50]="RE-MAIL-CK"
    [51]="IMP-LOG"
    [53]="DNS"
    [57]="MAILQ"
    [67]="DHCP-SERVER"
    [68]="DHCP-CLIENT"
    [69]="TFTP"
    [70]="GOPHER"
    [79]="FINGER"
    [80]="HTTP"
    [81]="HTTP-ALT"
    [88]="KERBEROS"
    [95]="SUPDUP"
    [101]="HOSTNAME"
    [102]="ISO-TSAP"
    [107]="RTELNET"
    [109]="POP2"
    [110]="POP3"
    [111]="RPCBIND"
    [113]="IDENT"
    [115]="SFTP"
    [117]="UUCP-PATH"
    [119]="NNTP"
    [123]="NTP"
    [135]="MSRPC"
    [137]="NETBIOS-NS"
    [138]="NETBIOS-DGM"
    [139]="NETBIOS-SSN"
    [143]="IMAP"
    [161]="SNMP"
    [162]="SNMP-TRAP"
    [163]="CMIP"
    [164]="CMIP-MAN"
    [179]="BGP"
    [194]="IRC"
    [199]="SMUX"
    [201]="APPLE-TALK"
    [209]="QMTP"
    [210]="Z39.50"
    [213]="IPX"
    [218]="MPP"
    [220]="IMAP3"
    [389]="LDAP"
    [427]="SLP"
    [443]="HTTPS"
    [444]="SNPP"
    [445]="SMB"
    [464]="KERBEROS-PASSWD"
    [465]="SMTPS"
    [497]="DANTZ"
    [500]="ISAKMP"
    [512]="EXEC"
    [513]="LOGIN"
    [514]="SHELL"
    [515]="LPD"
    [520]="RIP"
    [521]="RIPNG"
    [540]="UUCP"
    [548]="AFP"
    [554]="RTSP"
    [563]="NNTPS"
    [587]="SMTP-SUBMISSION"
    [591]="FILEMAKER"
    [593]="HTTP-RPC-EPMAP"
    [631]="CUPS"
    [636]="LDAPS"
    [639]="MSDP"
    [646]="LDP"
    [647]="DHCP-FAILOVER"
    [648]="RRP"
    [666]="DOOM"
    [691]="MS-EXCHANGE"
    [860]="ISCSI"
    [873]="RSYNC"
    [902]="VMWARE"
    [989]="FTPS-DATA"
    [990]="FTPS"
    [993]="IMAPS"
    [995]="POP3S"
    [1080]="SOCKS"
    [1194]="OPENVPN"
    [1241]="NESSUS"
    [1311]="RXMON"
    [1337]="WASTE"
    [1433]="MSSQL"
    [1434]="MSSQL-MONITOR"
    [1494]="CITRIX"
    [1512]="WINS"
    [1521]="ORACLE"
    [1604]="CITRIX-ICA"
    [1645]="RADIUS-OLD"
    [1646]="RADIUS-ACCT-OLD"
    [1701]="L2TP"
    [1720]="H323"
    [1723]="PPTP"
    [1755]="MMS"
    [1812]="RADIUS"
    [1813]="RADIUS-ACCT"
    [1863]="MSN"
    [1900]="UPNP"
    [2000]="CISCO-SCCP"
    [2002]="GLASSFISH"
    [2049]="NFS"
    [2082]="CPANEL"
    [2083]="CPANEL-SSL"
    [2086]="WHM"
    [2087]="WHM-SSL"
    [2095]="WEBMAIL"
    [2096]="WEBMAIL-SSL"
    [2100]="AMIGA"
    [2222]="SSH-ALT"
    [2375]="DOCKER"
    [2376]="DOCKER-SSL"
    [2483]="ORACLE-SSL"
    [2484]="ORACLE-TCP"
    [2601]="ZEBRA"
    [2604]="QUAGGA"
    [3000]="DEV-WEB"
    [3001]="DEV-WEB-ALT"
    [3128]="SQUID"
    [3260]="ISCSI-TARGET"
    [3306]="MYSQL"
    [3389]="RDP"
    [3690]="SVN"
    [4000]="ICQ"
    [4040]="SPARK"
    [4444]="METASPLOIT"
    [4567]="RUBY-DEBUG"
    [4662]="EDONKEY"
    [4899]="RADMIN"
    [5000]="UPNP-ALT"
    [5001]="IPERF"
    [5060]="SIP"
    [5061]="SIP-TLS"
    [5222]="XMPP"
    [5269]="XMPP-S2S"
    [5353]="MDNS"
    [5432]="POSTGRESQL"
    [5500]="VNC-ALT"
    [5601]="KIBANA"
    [5631]="PCANYWHERE"
    [5666]="NRPE"
    [5672]="AMQP"
    [5800]="VNC-HTTP"
    [5900]="VNC"
    [5985]="WINRM"
    [5986]="WINRM-SSL"
    [6000]="X11"
    [6379]="REDIS"
    [6443]="KUBERNETES-API"
    [6514]="SYSLOG-TLS"
    [6667]="IRC-ALT"
    [7001]="WEBLOGIC"
    [7002]="WEBLOGIC-SSL"
    [7070]="REALSERVER"
    [7199]="CASSANDRA-JMX"
    [7474]="NEO4J"
    [7777]="GAME-ALT"
    [8000]="HTTP-ALT"
    [8008]="HTTP-PROXY-ALT"
    [8080]="HTTP-PROXY"
    [8081]="HTTP-ALT3"
    [8086]="INFLUXDB"
    [8088]="HADOOP"
    [8090]="HTTP-ALT4"
    [8161]="ACTIVEMQ"
    [8200]="VAULT"
    [8332]="BITCOIN-RPC"
    [8333]="BITCOIN"
    [8443]="HTTPS-ALT"
    [8500]="CONSUL"
    [8530]="DNSMASQ"
    [8600]="CONSUL-DNS"
    [8888]="HTTP-DEV"
    [9000]="SONARQUBE"
    [9042]="CASSANDRA"
    [9090]="PROMETHEUS"
    [9092]="KAFKA"
    [9100]="PRINTER"
    [9200]="ELASTICSEARCH"
    [9418]="GIT"
    [9999]="DEBUG"
    [10000]="WEBMIN"
    [11211]="MEMCACHED"
    [15672]="RABBITMQ-MGMT"
    [27017]="MONGODB"
    [27018]="MONGODB-ALT"
    [27019]="MONGODB-CONFIG"
)


function ctrl_c(){
    echo "Saliendo...etc"
    exit 1
}

trap ctrl_c SIGINT

if [[ $# != 2 ]]; then
    echo "Uso: $0 <IP> <HILOS>"
    exit 1
fi

valida_ip(){
    ip=$1

    if [[ ! $ip =~ (^[0-9]{1,3}\.)[0-9]{1,3} ]]; then
        return 1
    fi

    IFS="." read -r o1 o2 o3 o4 <<< "$ip"

    for octeto in "$o1" "$o2" "$o3" "$o4"; do
        if (( octeto < 0 || octeto > 255 )); then
            return 1
        fi
    done
    return 0
}

valida_hilos(){
    hilos=$1

    if [[ ! $hilos =~ ^[0-9]+$ ]]; then
        return 1
    fi

    if (( hilos < 0 || hilos > 500 )); then
        return 1
    fi
    return 0
}

function port_scan(){
    IP=$1
    THREADS=$2

    for i in {1..65535}; do
        while (($(jobs -pr | wc -l) >= $THREADS)); do
            wait -n 2>/dev/null
        done

        if (echo > /dev/tcp/$IP/$i) &>/dev/null; then
            servicio=${SERVICIO[$i]:-Desconocido}
            echo "[+] Puerto $i abierto -> $servicio"
        fi &
    done
}

if ! valida_ip "$IP"; then
    echo "IP inválida"
    exit 1
fi

if ! valida_hilos "$THREADS"; then
    echo "Número de hilos incorrectos, mínimo 1 máximo 500"
    exit 1
fi

port_scan "$IP" "$THREADS"


















