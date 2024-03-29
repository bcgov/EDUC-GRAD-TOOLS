FROM alpine:3.7
MAINTAINER shea.phillips@cloudcompass.ca

RUN apk update && \
    apk upgrade

# ===================================================================================================================================================================
# Install Caddy
# Refs:
# - https://github.com/ZZROTDesign/alpine-caddy
# - https://github.com/mholt/caddy
# -------------------------------------------------------------------------------------------------------------------------------------------------------------------
RUN apk update && \
    apk --no-cache add \
        tini \
        git \
        openssh-client && \
    apk --no-cache add --virtual \
        devs \
        tar \
        curl

# Install Caddy Server, and All Middleware
RUN curl -L "https://github.com/arcshiftsolutions/s2i-caddy/blob/master/openshift/caddy_v0.10.10_linux_amd64.tar.gz?raw=true" \
    | tar --no-same-owner -C /usr/bin/ -xz caddy

# Remove build devs
RUN apk del devs

LABEL io.openshift.s2i.scripts-url=image:///tmp/scripts

## Copy the S2I scripts into place
COPY ./.s2i/bin/ /tmp/scripts

ADD Caddyfile /etc/Caddyfile

# Create the location where we will store our content, and fiddle the permissions so we will be able to write to it.
# Also twiddle the permissions on the Caddyfile and the s2i scripts dir so we will be able to overwrite them with a user-provided one sif desired.
RUN mkdir -p /var/www/html \
    && chmod -R 775 /var/www/html \
    && chmod -R 775 /etc/Caddyfile \
    && chmod -R 775 /tmp/scripts \
    && mkdir /tmp/src \
    && chmod -R 775 /tmp/src

# Work-around for issues with S2I builds on Windows
WORKDIR /tmp

# Expose the port for the container to Caddy's default
EXPOSE 2015

USER 1001

ENTRYPOINT ["/sbin/tini"]

CMD ["sh","/tmp/scripts/usage"]
