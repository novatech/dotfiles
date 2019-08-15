#!/bin/sh

# Read Xresources when exists
[[ -f ~/.Xresources ]] && xrdb -merge ~/.Xresources &> /dev/null

# no beep!
xset -b &> /dev/null

# wallpaper
hsetroot -fill ~/.xwallpaper

# set mouse cursor
xsetroot -cursor_name left_ptr &> /dev/null

if [ -d /etc/X11/xinit/xinitrc.d ]; then
  for f in /etc/X11/xinit/xinitrc.d/*; do
    [ -x "$f" ] && . "$f"
  done
  unset f
fi

exec $1 &> /dev/null

