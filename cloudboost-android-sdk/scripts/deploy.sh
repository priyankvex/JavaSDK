#!/bin/bash
set -e
chmod u+x .travis/deploy.sh
echo "checking git status"
git status
echo "finished checking status"
rev=$(git rev-parse --short HEAD)
git config user.name "egimaben"
git config user.email "egimaben@gmail.com"
git config user.password "soulsurvivor1"

git add .
git commit -m "travis commit at ${rev}"
git push origin master