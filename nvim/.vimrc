execute pathogen#infect()

" look & feels
colorscheme dracula
let g:dracula_italic=0

syn on se title
set nocompatible
set ignorecase
set nowrap
set wrapscan
set noswapfile
set viminfo="/dev/null"

" lets use the internet standard charset
set encoding=UTF-8

" use system clipboard
set clipboard^=unnamed,unnamedplus

" make whitespaces visible
set list listchars=tab:·⁖,trail:¶

" automatic removing all trailing whitepaces during :w
autocmd BufWritePre * %s/\s\+$//e

" enable traverse line breaks with arrow keys
set whichwrap=b,s,<,>,[,]

" syntax highlighting
syntax enable
filetype plugin indent on

" additional setting
set modeline           " enable vim modelines
set hlsearch           " highlight search items
set incsearch          " searches are performed as you type
set confirm            " ask confirmation like save before quit.
set wildmenu           " Tab completion menu when using command mode
set expandtab          " Tab key inserts spaces not tabs
set tabstop=4          " spaces to enter for each tab
set softtabstop=4      " spaces to enter for each tab
set shiftwidth=4       " amount of spaces for indentation
set linebreak breakindent
set number relativenumber
set splitbelow splitright
setlocal foldmethod=marker

" disable auto indent
set noautoindent
set nocindent
set nosmartindent
set indentexpr=

" disable automatic comment insertion
autocmd FileType * setlocal formatoptions-=c formatoptions-=r formatoptions-=o comments-=:// comments+=f://

" automatically turn on auto-save for markdown files
autocmd FileType markdown let g:auto_save = 0
autocmd FileType markdown inoremap ppp :point_right:<space>

" Quick insertion of a GitHub issues style checkbox item (- [ ] )
autocmd FileType markdown inoremap ii -<space>[<space>]<space>

" integrate Limelight with Goyo
autocmd! User GoyoEnter Limelight
autocmd! User GoyoLeave Limelight!

set cursorline
set nocursorcolumn
highlight CursorLine cterm=NONE ctermbg=black ctermfg=NONE guibg=darkgrey guifg=NONE
highlight CursorColumn cterm=NONE ctermbg=black ctermfg=NONE guibg=darkgrey guifg=NONE

" linux kernel formatting
set wildignore+=*.ko,*.mod.c,*.order,modules.builtin
autocmd FileType c,cc,cpp call s:LinuxFormatting()
autocmd FileType c,cc,cpp call s:LinuxKeywords()
autocmd FileType c,cc,cpp call s:LinuxHighlighting()
autocmd FileType diff,kconfig setlocal tabstop=8

function s:LinuxFormatting()
    setlocal tabstop=8
    setlocal shiftwidth=8
    setlocal textwidth=80
    setlocal noexpandtab

    setlocal cindent
    setlocal formatoptions=tcqlron
    setlocal cinoptions=:0,l1,t0,g0
endfunction

function s:LinuxKeywords()
    syn keyword cOperator likely unlikely
    syn keyword cType u8 u16 u32 u64 s8 s16 s32 s64
endfunction

hi LinuxError   cterm=NONE ctermbg=yellow ctermfg=black

function s:LinuxHighlighting()
    highlight default link LinuxError ErrorMsg
    highlight default link LinuxError ErrorMsg

    syn match LinuxError / \+\ze\t/     " spaces before tab
    syn match LinuxError /\s\+$/        " trailing whitespaces
    syn match LinuxError /\%81v.\+/     " virtual column 81 and more
endfunction

" end linux kernel formatting
