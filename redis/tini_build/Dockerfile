FROM redis:7.0.8

RUN apt-get update && apt-get install -y tini && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/usr/bin/tini", "--"]
