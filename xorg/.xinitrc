#!/bin/sh

# no mouse when idle
unclutter -root -reset -idle 1 -noevents &

# re-read font db
xset fp rehash &

# no beep!
xset -b &

# wallpaper
hsetroot -fill ~/.xwallpaper &

xset dpms 300 &
xss-lock -- i3lock --color 1e2541 &

# read Xresources when exists
[[ -f ~/.Xresources ]] && xrdb -load ~/.Xresources

if [ -d /etc/X11/xinit/xinitrc.d ]; then
    for f in /etc/X11/xinit/xinitrc.d/*; do
        [ -x "$f" ] && . "$f"
    done
    unset f
fi

exec $1 &> /dev/null

