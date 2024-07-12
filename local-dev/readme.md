# Local development support tools for GRAD
## Most GRAD apis rely on the support of services like nats and redis in order to function correctly. This directory contains scripts that provision dockerized support for developers local environments.
### Prerequisites
In order to use the scripts in this directory, it will require:

Windows:
    WSL2 Ubuntu 24xx installed
        After installing, a few things to make life easier, set up your linux environment
            1. ensure bash
                `chsh -s /bin/bash`
            2. install docker compose
                `sudo apt install docker-compose`
            3. enable docker without sudo
                `sudo groupadd docker`
                `sudo usermod -aG docker $USER`
    Docker Desktop version > v4.32.0
        Ensure certain features are enabled:
            Go to Settings / Resources / WSL integrations and ensure your WSL2 integration is selected
            Go to Settings / Features in development / Enable host networking

### Running the setup
    1. Ensure docker desktop is running
    2. Open your WSL linux distro and navigate to this director (remember your windows drives can be found under /mnt)
    3. Run the init-local-dev.sh script: `sudo ./init-local-dev.sh`


