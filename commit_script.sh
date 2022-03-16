#!/bin/bash

commit_message=$1

if [[ -n "$commit_message" ]]; then
    # re-generate the documentation
    ./gradlew dokkaHtml

    # move the documentation to the root directory, ./build/ is excluded in .gitignore
    if [ -f "./dokka" ]; then
      rm -rf ./dokka/
    fi

    mv ./build/dokka/ ./

    # commit to git
    git add .
    git commit -m "$commit_message"
else
  echo "please provide commit message, wrap in quotes."
fi
