# Contributing to SPICE

Thank you for considering contributing to the SPICE. We appreciate your interest in improving our project. This document outlines the guidelines and information you need to know to contribute effectively.

The SPICE App is the first open-source digital platform designed to support a new era of data-driven community healthcare delivery. It is a community-based platform for data-driven population health, leveraging clinical algorithms to screen, refer, and manage patients based on clinical risk. SPICE transforms care delivery from reactive to proactive and links community-based work bi-directionally to the health system.

We welcome contributions from developers, designers, and anyone passionate about community healthcare, data-driven solutions, and technology.

## Getting Started

Pull requests are the best way to propose changes to the codebase. We actively welcome your pull requests:

- Fork the repo and create your branch from master.
- Clone your forked repository to your local development environment.
- Set up the development environment. Please refer [README](README.md).
- Create a new branch for your changes.
- Make your modifications and improvements.
- If you've added code that should be tested, add tests.
- If you've changed APIs, update the documentation.
- Ensure the test suite passes.
- Make sure to check the code lint errors.
- Commit your changes with clear and concise commit messages.
- Push your changes to your forked repository.
- Submit a pull request to the main repository.

## Reporting Bugs

1. Please check to see if you are running the latest version of SPICE; the bug may already be resolved. If you are running an unsupported version, it may no longer be supported. Check our [README](README.md) for details.

2. Search for similar problems using [GitHub issue search](https://github.com/Medtronic-LABS/spice-server/issues); it may already be an identified problem.

3. Make sure you can reproduce your problem using the latest master branch in the repository.

4. If you believe the problem is with SPICE itself, or if you're honestly not sure, you can create an issue from [GitHub Issue](https://github.com/Medtronic-LABS/spice-server/issues) page.

### Bug report contents

Please include as much information as possible to help those developers understand the problem or to report the bug to the SPICE core team:

1. A clear description of how to recreate the error, including any error messages shown.
2. The version of SPICE you are using and the necessary packages and libraries version.
3. The type and version of the database you are using, if known.
4. If you are using any additional modules, and other libraries, please list them.
5. If applicable, please copy and paste the full Java stack trace.
6. If you have already communicated with a developer about this issue, please provide their name.

## Requesting New Features

1. We encourage you to discuss your feature ideas with our community before creating a New Feature issue in our issue tracker.

   Scan through our [GitHub existing Issues](https://github.com/Medtronic-LABS/spice-server/issues) to find one that interests you. You can narrow down the search using `labels` as filters. As a general rule, we don’t assign issues to anyone. If you find an issue to work on, you are welcome to open a PR with a fix.

2. Provide a clear and detailed explanation of the feature you want and why it's important to add. The feature must apply to a wide array of users of SPICE. You may also want to provide us with some advance documentation on the feature, which will help the community to better understand where it will fit.

3. If you're an advanced programmer, build the feature yourself (refer to the "Contributing (Step-by-step)" section below).

## Contributing

To get started with contributing to the SPICE App, follow these steps:

If you want to work on an existing issue, please leave a comment on the issue indicating your intention. This helps avoid duplicate efforts and allows the project maintainers to provide guidance and feedback.

1.  To fork the repository you need to have a GitHub account. Once you have an account you can click the fork button up top. Now that you have your fork you need to clone it.

        git clone https://github.com/{username}/spice-server.git
        git remote add upstream https://github.com/Medtronic-LABS/spice-server.git

2.  Checkout to a new local branch based on your master and update it to the latest. The convention is to name the branch after the current issue under the type of issue, e.g. bug/click-issue

        git checkout -b {branch name} master
        git clean -df
        git pull --rebase upstream master

> Please keep your code clean. Name your branch after the issue or other description of the work being done. If you find another bug, you want to fix while being in a new branch, please fix it in a separated branch instead.

3.  Push the branch to your fork. Treat it as a backup.

        git push origin {branch name}

4.  Code

- Adhere to common conventions you see in the existing code
- Include tests, and ensure they pass

5.  Commit

    For every commit please write a short (max 72 characters) summary in the first line with

           {Type of issue}: {Short summary}

    followed with a blank line and then more detailed descriptions of the change. Use markdown syntax for simple styling.

            git commit -m "FIX: Put change summary here"
              - Detailed description here.

- Reference any relevant issues or feature requests in the pull request description.
- Include appropriate tests for your changes.
- Make sure the codebase adheres to the coding standards.
- Push your changes to your forked repository and submit a pull request to the main repository.

### Coding Standards

To maintain code consistency and readability, please adhere to the following guidelines:

- Follow the appropriate coding style for each technology used (e.g., JavaScript/React.js coding conventions, Java coding conventions).
- Write meaningful variable and function names.
  Add comments to clarify complex code sections or provide additional context.

**NEVER leave the commit message blank!** Provide a detailed, clear, and complete description of your commit!

### Raising a Pull Request

Before submitting a pull request, update your branch to the latest code.

        git pull --rebase upstream master

Most of the tickets should have one commit only, especially bug fixes, which makes them easier to back port.

After building the application, the test cases should execute without any issues.

        mvn clean install    

Push changes to your fork:

        git push -f

In order to make a pull request,

- Navigate to the SPICE repository you just pushed to (e.g. https://github.com/{username}/spice-server.git)
- Click "Pull Request".
- Select the proper branch name in the branch field with proper repository(this is filled with "master" by default)
- Ensure the changesets you introduced are included in the "Commits".
- Ensure that the "Files Changed" incorporate all of your changes.
- Fill in some details about your potential patch including a meaningful title.
- Click "Create pull request".

Thanks for that -- we'll get to your pull request ASAP.

## Responding to Feedback

The SPICE team may recommend adjustments to your code. Part of interacting with a healthy open source community requires you to be open to learning new techniques and strategies; Remember: if the SPICE team suggests changes to your code, **they care enough about your work that they want to include it**, and hope that you can assist by implementing those revisions on your own.

> Though we ask you to clean your history and squash commit before submitting a pull-request, please do not change any commits you've submitted already (as other work might be build on top).

## Other Resources

- Make sure you have a [GitHub account](https://github.com/signup/free)
- Fork [SPICE SERVER](https://github.com/Medtronic-LABS/spice-server) or the repository for the module you are working on

### Interact with the community

- Contact with developers on [developer@medtroniclabs.org](developer@medtroniclabs.org)
- Join our [developers mailing list]()

### Additional Resources

- [General GitHub documentation](http://help.github.com/)
- [GitHub pull request documentation](http://help.github.com/send-pull-requests/)
