# Local development support tools for GRAD
Most GRAD apis rely on the support of services like nats and redis in order to function correctly. This directory contains scripts that provision dockerized support for developers local environments.
### Prerequisites
In order to use the scripts in this directory, it will require:

### Windows

**Install Ubuntu 24.xx.xx using WSL2**. Information can be found [here](https://learn.microsoft.com/en-us/windows/wsl/install)

After Ubuntu install, a few things to make life easier, set up your linux environment
1. ensure bash is your default shell
`chsh -s /bin/bash`
2. install docker compose
`sudo apt install docker-compose`
3. enable docker without sudo
`sudo groupadd docker`
`sudo usermod -aG docker $USER`

**Install Docker Desktop version > v4.32.0**
Ensure the following features are enabled:
Go to *Settings / Resources / WSL integrations* and ensure your WSL2 Ubuntu 24.xx.xx integration is selected
Go to *Settings / Features in development / Enable host networking*

### Running the setup
1. Ensure docker desktop is running
2. Open your WSL linux distro and navigate to this directory (remember your windows drives can be found under /mnt)
3. Run the init-local-dev.sh script: `sudo ./init-local-dev.sh`

The script will provision the necessary volume structure and set up the necessary docker containers etc.


