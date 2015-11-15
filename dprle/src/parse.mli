type token =
  | LBRACKET
  | RBRACKET
  | LBRACE
  | RBRACE
  | LPAREN
  | RPAREN
  | COMMA
  | ARROW
  | QUOTE
  | SUBSET
  | SEMICOLON
  | PUSH
  | POP
  | SOLVE
  | SOLVEALL
  | RESET
  | ON
  | ANY
  | DOT
  | EPSILON
  | START
  | FINAL
  | DELTA
  | UNKNOWN
  | ALIAS
  | NEG
  | CHARACTERSTART
  | QSUBSET
  | QEQUAL
  | IDENT of (string)
  | SYMBOL of (string)
  | INDEX of (string)
  | SELECT of (int)
  | MACHINES of (string list)
  | STRINGS of (int * (string list))

val statement :
  (Lexing.lexbuf  -> token) -> Lexing.lexbuf -> unit
