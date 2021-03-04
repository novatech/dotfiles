# If not running interactively, don't do anything
[[ $- != *i* ]] && return

# load custom setting that not suppose to be commit
[ -f ~/.custom ] && source ~/.custom

shopt -s checkhash checkwinsize
shopt -s dotglob extglob nocaseglob
shopt -s huponexit checkjobs
shopt -s cmdhist histappend histverify

codi() {
  local syntax="${1:-python}"
  shift
  vim -c \
    "let g:startify_disable_at_vimenter = 1 |\
    set bt=nofile ls=0 noru nonu nornu |\
    hi ColorColumn ctermbg=NONE |\
    hi VertSplit ctermbg=NONE |\
    hi NonText ctermfg=0 |\
    Codi $syntax" "$@"
}

if [[ $TERM == xterm-termite ]]; then
    . /etc/profile.d/vte.sh
    __vte_prompt_command
fi
export TERM=xterm-256color

function git_prompt() {
    local output;
    output="$(git branch 2>/dev/null | perl -ne '/^\* (.*)/ && print $1')"
    [ "$output" ] && printf " \[\033[0;31m\]\\uf126 $output"
}

function prompt_command() {
    PS1="\[\033[0;37m\]\342\224\214\342\224\200\$([[ \$? != 0 ]] && echo \"[\[\033[0;31m\]\342\234\227\[\033[0;37m\]]\342\224\200\")[\[\033[0;33m\]\u\[\033[0;37m\]:\[\033[0;96m\]\h\[\033[0;37m\]]\342\224\200[\[\033[0;32m\]\w\[\033[0;37m\]]$(git_prompt)\n\[\033[0;37m\]\342\224\224\342\224\200\342\224\200\342\225\274 \[\033[0m\]"
}

PROMPT_COMMAND="prompt_command"

# don't put duplicate lines in the history. See bash(1) for more options
# ... or force ignoredups and ignorespace
export HISTCONTROL=ignoredups:ignorespace:ignoreboth:erasedups
export HISTIGNORE="&:[bf]g:rm:ls:pwd:cd ..:cd ~-:cd -:cd:jobs:set -x:ls:history"
export HISTSIZE=120000
export HISTFILESIZE=120000

history -a

# enable vi mode
set -o vi

# enable color support of ls and also add handy aliases
if [ -x /usr/bin/dircolors ]; then
    test -r ~/.dircolors && eval "$(dircolors -b ~/.dircolors)" || eval "$(dircolors -b)"
    alias ls='ls --color=auto -h --group-directories-first'
    alias dir='dir --color=auto'
    alias vdir='vdir --color=auto'

    alias grep='grep -n --color=auto'
    alias fgrep='fgrep -n --color=auto'
    alias egrep='egrep -n --color=auto'
fi

# some more ls aliases
alias ll='ls -alF'
alias la='ls -A'
alias l='ls -CF'
alias rm='timeout 3 rm -Iv --one-file-system'
alias mv='timeout 8 mv -iv'
alias cp='timeout 8 cp -iv'
alias mkdir='mkdir -pv'
alias less='less -FSRXc'
alias ~='cd ~'
alias c='clear'
alias lr='ls -R | grep ":$" | sed -e '\''s/:$//'\'' -e '\''s/[^-][^\/]*\//--/g'\'' -e '\''s/^/   /'\'' -e '\''s/-/|/'\'' | less'

[ -r /usr/share/bash-completion/bash_completion ] && . /usr/share/bash-completion/bash_completion

# GPG start
unset SSH_AGENT_PID
if [ "${gnupg_SSH_AUTH_SOCK_by:-0}" -ne $$ ]; then
    export SSH_AUTH_SOCK="$(gpgconf --list-dirs agent-ssh-socket)"
fi

export GPG_TTY=$(tty)
# GPG end

export EDITOR=/usr/bin/vim
export VISUAL=$EDITOR
export BROWSER=/usr/bin/firefox
export TERMINAL=/usr/bin/termite
export XDG_DATA_HOME=$HOME/.local/share
export XDG_CONFIG_HOME=$HOME/.config
export XDG_CACHE_HOME=$HOME/.cache

export PATH="$HOME/.notify:$HOME/.node_modules/bin:$PATH"
export npm_config_prefix=~/.node_modules


man() {
    env LESS_TERMCAP_mb=$'\E[01;31m' \
        LESS_TERMCAP_md=$'\E[01;38;5;74m' \
        LESS_TERMCAP_me=$'\E[0m' \
        LESS_TERMCAP_se=$'\E[0m' \
        LESS_TERMCAP_so=$'\E[38;5;246m' \
        LESS_TERMCAP_ue=$'\E[0m' \
        LESS_TERMCAP_us=$'\E[04;38;5;146m' \
        man "$@"
    }

