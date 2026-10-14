# Contributing

Thanks for helping. Contributions are made through pull requests from your own fork.

## How to contribute with a fork

1. **Open an issue first** for new components or token changes, so we can agree on the need.
   Small fixes (typos, docs, obvious bugs) can go straight to a pull request.
2. **Fork** the repository: click **Fork** on https://github.com/liconrepo/MobileDesignSystem, or with the GitHub CLI:

   ```sh
   gh repo fork liconrepo/MobileDesignSystem --clone
   ```

3. **Clone your fork** (skip if you used `--clone`) and add the original repository as `upstream`:

   ```sh
   git clone https://github.com/<your-username>/MobileDesignSystem.git
   cd MobileDesignSystem
   git remote add upstream https://github.com/liconrepo/MobileDesignSystem.git
   ```

4. **Create a branch** from an up-to-date `main`. Never work on `main` directly:

   ```sh
   git fetch upstream
   git checkout -b feat/short-description upstream/main
   ```

5. **Make your change.** Read [docs/principles.md](docs/principles.md) and
   [docs/adding-a-component.md](docs/adding-a-component.md) first. Keep changes small and focused.
6. **Verify locally**:

   ```sh
   ./gradlew :designsystem:test :designsystem:lint :sample:assembleDebug
   ```

7. **Commit and push** to your fork:

   ```sh
   git add .
   git commit -m "Add short description of the change"
   git push -u origin feat/short-description
   ```

8. **Open a pull request** from your branch to `liconrepo/MobileDesignSystem` `main`. Describe what changed and
   why, link the issue (`Closes #123`), and include screenshots (light and dark) for visual changes.
9. **Respond to review.** Push more commits to the same branch and the pull request updates itself.

### Keeping your fork in sync

```sh
git fetch upstream
git checkout main
git merge --ff-only upstream/main
git push origin main
```

If `main` moved while your pull request is open, rebase your branch and force-push it:

```sh
git fetch upstream
git rebase upstream/main
git push --force-with-lease
```

## Checklist

- The Gradle command above passes.
- Visual changes include light and dark screenshots.
- Only submit work you have the right to contribute. Do not copy code, assets, tokens or documentation from
  proprietary or internal systems, including those of any current or former employer.

By contributing you agree your contribution is licensed under the MIT License.
