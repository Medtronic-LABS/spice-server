#!/usr/bin/env bash
# Generates the RSA keypair the services use for token encryption (private: PKCS#8 DER, public: X.509 DER).
# Keys are written to ./keys (git-ignored). Never commit them; in production load them from a secret store.
set -euo pipefail
dir="${1:-keys}"
mkdir -p "$dir"
[ -e "$dir/private_key.der" ] && { echo "$dir/private_key.der exists; remove it first to regenerate" >&2; exit 1; }
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -outform DER -out "$dir/private_key.der"
openssl pkey -inform DER -in "$dir/private_key.der" -pubout -outform DER -out "$dir/public_key.der"
chmod 600 "$dir/private_key.der"
echo "Wrote $dir/private_key.der and $dir/public_key.der"
