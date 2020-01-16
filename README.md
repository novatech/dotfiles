# . files

## setup
This is a Archlinux with i3wm setup for my personal laptop.
Any customization can go under ~/.custom file. For example,

```bash
touch ~/.gitconfig
GIT_AUTHOR_NAME="novatech"
GIT_AUTHOR_EMAIL="azwan.ali@pm.me"
GIT_COMITTER_NAME="$GIT_AUTHOR_NAME"
GIT_COMMITTER_EMAIL="$GIT_AUTHOR_EMAIL"
git config --global user.name "$GIT_AUTHOR_NAME"
git config --global user.email "$GIT_AUTHOR_EMAIL"
```

setup with stow
```bash
for i in $(ls);do stow -Sv $i;done
```

## screenshot
![rice](https://github.com/novatech/dotfiles/blob/master/.ss/beras.small.png?raw=true)
