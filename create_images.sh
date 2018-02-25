#!/bin/bash
set -e

sh -c 'cd stocks-api && ./create_image.sh'
sh -c 'cd stocks-ui && ./create_image.sh'