[Unit]
Description=formdang-sp-was
After=network.target

[Service]
EnvironmentFile=/home/sp/deploy/web/web.conf
Type=simple

WorkingDirectory=/home/sp/deploy/web
ExecStart=/usr/bin/java $JAVA_OPTS -jar "/home/sp/deploy/web/app.jar" --logging.path=$LOG_FOLDER $RUN_ARGS

SuccessExitStatus=143
Restart=on-failure
StandardOutput=journal
StandardError=journal
RestartSec=5

[Install]
