#!/bin/bash
npm run build
distPath=$(pwd)
cd ../../web/src/main/resources/
rm -rf public
mkdir -p public
cp -r ${distPath}/dist/* public/