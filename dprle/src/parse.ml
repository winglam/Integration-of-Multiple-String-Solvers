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

open Parsing;;
let _ = parse_error;;
# 2 "parse.mly"
(*  Copyright (c) 2008-2009, University of Virginia
    All rights reserved.
   
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
       * Redistributions of source code must retain the above copyright
         notice, this list of conditions and the following disclaimer.
       * Redistributions in binary form must reproduce the above
         copyright notice, this list of conditions and the following
         disclaimer in the documentation and/or other materials
         provided with the distribution.
       * Neither the name of the University of Virginia  nor the names 
         of its contributors may be used to endorse or promote products
         derived from this software without specific prior written
         permission.
   
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
    FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
    COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
    HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
    STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
    OF THE POSSIBILITY OF SUCH DAMAGE.
   
    Author: Pieter Hooimeijer
*)
  open Lexing
  open Depgraph
  open Interface

  type nfa = Nfa.nfa
  
  let tempcount = ref 0

  let new_temp () = 
    incr tempcount ;
    "_t" ^ string_of_int(!tempcount)

  let parse_error s =
    flush stdout

  let curnfa : (nfa option) ref = ref None
  let cur_int = ref 0
  let name_mapping = Hashtbl.create 500

  let to_int_state (q : string) : int =
    try Hashtbl.find name_mapping q with
	Not_found ->
	  Hashtbl.replace name_mapping q !cur_int;
	  incr cur_int;
	  !cur_int - 1
    
  let reset_int_state () : unit =
    cur_int := 0;
    Hashtbl.clear name_mapping

  let output_error b e message = 
    let startpos = Parsing.rhs_start_pos b in
    let endpos  = Parsing.rhs_end_pos e in
      Printf.printf "\n# Error (%d.%d-%d.%d): %s\n"
	startpos.pos_lnum (startpos.pos_cnum - startpos.pos_bol)
	endpos.pos_lnum   (endpos.pos_cnum - endpos.pos_bol)
	message;
      curnfa := None;
      reset_int_state ();
      raise Options.Known_error

  let wrong_state b e =
    output_error b e "Use select(n) to choose an assignment first"

  let cur_graph = Interface.cur_graph
# 119 "parse.ml"
let yytransl_const = [|
  257 (* LBRACKET *);
  258 (* RBRACKET *);
  259 (* LBRACE *);
  260 (* RBRACE *);
  261 (* LPAREN *);
  262 (* RPAREN *);
  263 (* COMMA *);
  264 (* ARROW *);
  265 (* QUOTE *);
  266 (* SUBSET *);
  267 (* SEMICOLON *);
  268 (* PUSH *);
  269 (* POP *);
  270 (* SOLVE *);
  271 (* SOLVEALL *);
  272 (* RESET *);
  273 (* ON *);
  274 (* ANY *);
  275 (* DOT *);
  276 (* EPSILON *);
  277 (* START *);
  278 (* FINAL *);
  279 (* DELTA *);
  280 (* UNKNOWN *);
  281 (* ALIAS *);
  282 (* NEG *);
  283 (* CHARACTERSTART *);
  284 (* QSUBSET *);
  285 (* QEQUAL *);
    0|]

let yytransl_block = [|
  286 (* IDENT *);
  287 (* SYMBOL *);
  288 (* INDEX *);
  289 (* SELECT *);
  290 (* MACHINES *);
  291 (* STRINGS *);
    0|]

let yylhs = "\255\255\
\001\000\001\000\001\000\001\000\001\000\001\000\001\000\001\000\
\003\000\003\000\003\000\003\000\002\000\006\000\006\000\006\000\
\006\000\005\000\005\000\005\000\005\000\005\000\005\000\007\000\
\007\000\008\000\008\000\008\000\008\000\008\000\008\000\008\000\
\008\000\008\000\009\000\009\000\009\000\010\000\010\000\011\000\
\011\000\011\000\004\000\004\000\004\000\004\000\004\000\004\000\
\004\000\004\000\004\000\004\000\013\000\013\000\014\000\014\000\
\012\000\012\000\012\000\012\000\000\000"

let yylen = "\002\000\
\003\000\004\000\004\000\004\000\004\000\006\000\002\000\002\000\
\001\000\001\000\002\000\002\000\001\000\001\000\002\000\003\000\
\003\000\002\000\003\000\004\000\005\000\006\000\008\000\000\000\
\002\000\001\000\002\000\003\000\004\000\005\000\005\000\005\000\
\005\000\006\000\002\000\003\000\004\000\001\000\003\000\001\000\
\001\000\002\000\004\000\003\000\003\000\003\000\003\000\003\000\
\004\000\004\000\004\000\006\000\000\000\001\000\001\000\003\000\
\000\000\001\000\001\000\001\000\002\000"

let yydefred = "\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\061\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\007\000\008\000\015\000\000\000\
\044\000\045\000\046\000\047\000\048\000\060\000\055\000\058\000\
\000\000\000\000\054\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\001\000\000\000\000\000\009\000\000\000\010\000\
\016\000\017\000\043\000\000\000\000\000\004\000\005\000\049\000\
\050\000\000\000\051\000\018\000\000\000\011\000\012\000\002\000\
\003\000\056\000\000\000\000\000\019\000\000\000\006\000\052\000\
\020\000\000\000\021\000\000\000\022\000\024\000\000\000\026\000\
\023\000\000\000\025\000\027\000\000\000\028\000\000\000\029\000\
\000\000\030\000\000\000\031\000\032\000\000\000\033\000\040\000\
\035\000\000\000\041\000\000\000\038\000\034\000\042\000\036\000\
\000\000\037\000\039\000"

let yydgoto = "\002\000\
\012\000\013\000\055\000\014\000\056\000\015\000\087\000\091\000\
\103\000\108\000\109\000\041\000\042\000\043\000"

let yysindex = "\055\000\
\038\255\000\000\077\255\078\255\079\255\080\255\081\255\000\255\
\082\255\083\255\084\255\000\000\051\255\064\255\048\255\085\255\
\086\255\087\255\088\255\089\255\002\255\060\255\067\255\068\255\
\069\255\070\255\244\254\012\255\000\000\000\000\000\000\014\255\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\090\255\092\255\000\000\091\255\093\255\094\255\096\255\056\255\
\099\255\063\255\000\000\003\255\021\255\000\000\066\255\000\000\
\000\000\000\000\000\000\073\255\095\255\000\000\000\000\000\000\
\000\000\070\255\000\000\000\000\015\255\000\000\000\000\000\000\
\000\000\000\000\097\255\072\255\000\000\036\255\000\000\000\000\
\000\000\016\255\000\000\007\255\000\000\000\000\009\255\000\000\
\000\000\019\255\000\000\000\000\017\255\000\000\059\255\000\000\
\023\255\000\000\004\255\000\000\000\000\104\255\000\000\000\000\
\000\000\098\255\000\000\053\255\000\000\000\000\000\000\000\000\
\006\255\000\000\000\000"

let yyrindex = "\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\055\255\
\000\000\000\000\000\000\000\000\000\000\000\000\101\255\000\000\
\000\000\000\000\000\000\000\000\074\255\000\000\000\000\000\000\
\000\000\074\255\074\255\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\090\255\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\074\255\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000"

let yygindex = "\000\000\
\000\000\000\000\000\000\000\000\056\000\000\000\000\000\000\000\
\010\000\000\000\001\000\000\000\230\255\000\000"

let yytablesize = 130
let yytable = "\048\000\
\050\000\038\000\068\000\104\000\021\000\104\000\085\000\105\000\
\088\000\114\000\089\000\051\000\052\000\057\000\077\000\083\000\
\094\000\039\000\092\000\049\000\070\000\052\000\098\000\069\000\
\022\000\099\000\093\000\023\000\024\000\086\000\106\000\039\000\
\106\000\040\000\107\000\081\000\107\000\053\000\090\000\076\000\
\100\000\054\000\101\000\058\000\078\000\084\000\095\000\031\000\
\102\000\003\000\004\000\005\000\006\000\007\000\014\000\001\000\
\112\000\082\000\096\000\113\000\028\000\065\000\060\000\029\000\
\014\000\072\000\032\000\008\000\067\000\060\000\009\000\010\000\
\011\000\014\000\030\000\097\000\073\000\080\000\060\000\053\000\
\053\000\016\000\017\000\018\000\019\000\020\000\025\000\026\000\
\027\000\044\000\033\000\034\000\035\000\036\000\037\000\059\000\
\045\000\046\000\060\000\039\000\047\000\064\000\074\000\062\000\
\063\000\066\000\099\000\079\000\071\000\061\000\013\000\110\000\
\000\000\115\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\075\000\000\000\000\000\000\000\
\000\000\111\000"

let yycheck = "\026\000\
\027\000\000\001\000\001\000\001\005\001\000\001\000\001\004\001\
\000\001\004\001\002\001\000\001\001\001\000\001\000\001\000\001\
\000\001\030\001\000\001\032\001\000\001\001\001\000\001\021\001\
\025\001\003\001\008\001\028\001\029\001\023\001\027\001\030\001\
\027\001\032\001\031\001\000\001\031\001\026\001\030\001\066\000\
\018\001\030\001\020\001\030\001\030\001\030\001\030\001\000\001\
\026\001\012\001\013\001\014\001\015\001\016\001\000\001\001\000\
\004\001\022\001\000\001\007\001\010\001\006\001\007\001\000\001\
\010\001\000\001\019\001\030\001\006\001\007\001\033\001\034\001\
\035\001\019\001\011\001\017\001\011\001\006\001\007\001\006\001\
\007\001\005\001\005\001\005\001\005\001\005\001\005\001\005\001\
\005\001\030\001\006\001\006\001\006\001\006\001\006\001\006\001\
\030\001\030\001\007\001\030\001\032\001\006\001\030\001\011\001\
\011\001\007\001\003\001\011\001\053\000\019\001\010\001\102\000\
\255\255\113\000\255\255\255\255\255\255\255\255\255\255\255\255\
\255\255\255\255\255\255\255\255\030\001\255\255\255\255\255\255\
\255\255\032\001"

let yynames_const = "\
  LBRACKET\000\
  RBRACKET\000\
  LBRACE\000\
  RBRACE\000\
  LPAREN\000\
  RPAREN\000\
  COMMA\000\
  ARROW\000\
  QUOTE\000\
  SUBSET\000\
  SEMICOLON\000\
  PUSH\000\
  POP\000\
  SOLVE\000\
  SOLVEALL\000\
  RESET\000\
  ON\000\
  ANY\000\
  DOT\000\
  EPSILON\000\
  START\000\
  FINAL\000\
  DELTA\000\
  UNKNOWN\000\
  ALIAS\000\
  NEG\000\
  CHARACTERSTART\000\
  QSUBSET\000\
  QEQUAL\000\
  "

let yynames_block = "\
  IDENT\000\
  SYMBOL\000\
  INDEX\000\
  SELECT\000\
  MACHINES\000\
  STRINGS\000\
  "

let yyact = [|
  (fun _ -> failwith "parser")
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'expr) in
    Obj.repr(
# 94 "parse.mly"
             ( output_error 3 3 "Expected identifier or machine definition" )
# 327 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : 'expr) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'rhs) in
    Obj.repr(
# 96 "parse.mly"
      ( output_error 4 4 "Expected ';'" )
# 335 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : 'expr) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'rhs) in
    Obj.repr(
# 98 "parse.mly"
             ( let varname, is_nfa = _3 in
		 try 
		   intersect varname _1
		 with WrongState -> wrong_state 1 4
	     )
# 347 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 104 "parse.mly"
        ( (try issubset _1 _3 with
		    | WrongState -> wrong_state 1 4
		    | BadIdent v -> output_error 1 4 ("Identifier '" ^ v ^ "' is unbound"))
               )
# 358 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 109 "parse.mly"
               ( (try areequal _1 _3 with
		    | WrongState -> wrong_state 1 4
		    | BadIdent v -> output_error 1 4 ("Identifier '" ^ v ^ "' is unbound"))
	       )
# 369 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 5 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _5 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 113 "parse.mly"
                                            (
	       (try named_concat _3 _5 _1 
		with CantAlias -> 
		  output_error 1 4 ("Node " ^ _1 ^ " already represents a concatenation")
		  | WrongState -> wrong_state 1 4)
	     )
# 383 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : 'command) in
    Obj.repr(
# 120 "parse.mly"
      ( output_error 2 2 "Expected ';'" )
# 390 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : 'command) in
    Obj.repr(
# 123 "parse.mly"
      ( match _1 with 
		 | PUSH -> push()
		 | POP  -> (try pop () with Pop -> 
			      output_error 1 2 "Too many calls to pop()")
		 | SOLVE ->
		     (try solve ((Parsing.rhs_start_pos 1).pos_lnum) false
		      with WrongState -> wrong_state 1 2)
		 | SOLVEALL ->
		     (try solve ((Parsing.rhs_start_pos 1).pos_lnum) true
		      with WrongState -> wrong_state 1 2)
		 | RESET ->
		     reset_all ();
		 | SELECT n ->
		     (try 
			select n
		      with WrongState -> output_error 1 2 "Only the current solution is available"
			| BadSelectIndex -> output_error 1 2 "Index out of range")
		 | MACHINES idl ->
		     (try
			show_machines idl
		      with WrongState -> wrong_state 1 2
			| BadIdent v -> output_error 1 2 ("Identifier '" ^ v ^ "' is unbound"))
		 | STRINGS (number, identlist) ->
		     (try 
			gen_strings identlist
		      with WrongState -> wrong_state 1 2
			| BadIdent v -> output_error 1 2 ("Identifier '" ^ v ^ "' is unbound"))
		 | _ -> failwith "Not possible."
	     )
# 425 "parse.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 153 "parse.mly"
                 ( (_1, false) )
# 432 "parse.ml"
               : 'rhs))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'machinedef) in
    Obj.repr(
# 155 "parse.mly"
      ( let varname = new_temp () in 
		 ignore (try new_node varname (Machine _1)
		  with WrongState -> wrong_state 1 1);
		 (varname, true)
	     )
# 443 "parse.ml"
               : 'rhs))
; (fun __caml_parser_env ->
    Obj.repr(
# 160 "parse.mly"
                (
	       output_error 2 2 "Expected NFA definition"
	     )
# 451 "parse.ml"
               : 'rhs))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 0 : 'machinedef) in
    Obj.repr(
# 164 "parse.mly"
      ( let varname = new_temp () in
	       let machine = (Nfa.nfa_to_dfa _2) in
		 Nfa.complement machine;
		 ignore (try (new_node varname (Machine machine)) with
		    WrongState -> wrong_state 1 2);
		 (varname, true)
	      )
# 464 "parse.ml"
               : 'rhs))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'concatlist) in
    Obj.repr(
# 172 "parse.mly"
                    ( _1 )
# 471 "parse.ml"
               : 'expr))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 174 "parse.mly"
                   ( _1 )
# 478 "parse.ml"
               : 'concatlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : 'concatlist) in
    Obj.repr(
# 176 "parse.mly"
             ( output_error 2 2 "Expected '.' or 'subset'" )
# 485 "parse.ml"
               : 'concatlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'concatlist) in
    Obj.repr(
# 178 "parse.mly"
      ( output_error 3 3 "Expected identifier" )
# 492 "parse.ml"
               : 'concatlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'concatlist) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 180 "parse.mly"
      ( let varname = new_temp () in
		 anon_concat _1 _3 varname; 
		 varname
             )
# 503 "parse.ml"
               : 'concatlist))
; (fun __caml_parser_env ->
    Obj.repr(
# 186 "parse.mly"
      ( output_error 2 2 "Expected 's:'" )
# 509 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    Obj.repr(
# 188 "parse.mly"
      ( output_error 3 3 "Expected identifier" )
# 515 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 190 "parse.mly"
             ( output_error 4 4 "Expected 'f:'" )
# 522 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 2 : string) in
    Obj.repr(
# 192 "parse.mly"
      ( output_error 5 5 "Expected identifier" )
# 529 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _5 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 194 "parse.mly"
      ( output_error 6 6 "Expected 'd:'" )
# 537 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 5 : string) in
    let _5 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _7 = (Parsing.peek_val __caml_parser_env 1 : 'transitionlist) in
    Obj.repr(
# 196 "parse.mly"
      ( let newnfa = match !curnfa with 
                 | None -> 
		     Nfa.new_nfa_states (to_int_state _3) (to_int_state _5)
                 | Some nfa -> 
		     nfa.Nfa.s <- (to_int_state _3);
		     nfa.Nfa.f <- (to_int_state _5); nfa
	       in
		 curnfa := None; 
		 reset_int_state ();
		 newnfa
	     )
# 556 "parse.ml"
               : 'machinedef))
; (fun __caml_parser_env ->
    Obj.repr(
# 208 "parse.mly"
                            ( )
# 562 "parse.ml"
               : 'transitionlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : 'transitionlist) in
    let _2 = (Parsing.peek_val __caml_parser_env 0 : 'transition) in
    Obj.repr(
# 209 "parse.mly"
                                       ( )
# 570 "parse.ml"
               : 'transitionlist))
; (fun __caml_parser_env ->
    Obj.repr(
# 212 "parse.mly"
             ( output_error 1 1 ("Expected ']' or transition of the form " ^
		 "'IDENT -> IDENT on { SYMBOLS }'")
	     )
# 578 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 216 "parse.mly"
      ( output_error 2 2 "Expected '->'" )
# 585 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : string) in
    Obj.repr(
# 218 "parse.mly"
      ( output_error 3 3 "Expected identifier" )
# 592 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 220 "parse.mly"
      ( output_error 4 4 "Expected 'on'" )
# 600 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 4 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 2 : string) in
    Obj.repr(
# 222 "parse.mly"
      ( output_error 5 5 
		 "Expected {}-delimited set of characters, 'epsilon', 'any', or 'neg'" 
             )
# 610 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 4 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 2 : string) in
    Obj.repr(
# 226 "parse.mly"
      ( let nfa = match !curnfa with 
		 | None ->
		     let newnfa = Nfa.new_nfa_states 0 1 in
		       curnfa := (Some newnfa); 
		       newnfa
		 | Some nfa -> nfa
	       in
		 Nfa.add_all_trans nfa (to_int_state _1) (to_int_state _3)
	     )
# 626 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 4 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 2 : string) in
    Obj.repr(
# 236 "parse.mly"
      ( let nfa = match !curnfa with 
		 | None ->
		     let newnfa = Nfa.new_nfa_states 0 1 in
		       curnfa := (Some newnfa); 
		       newnfa
		 | Some nfa -> nfa
	       in
		 Nfa.add_trans nfa (to_int_state _1) Nfa.Epsilon (to_int_state _3)
	     )
# 642 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 4 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 2 : string) in
    let _5 = (Parsing.peek_val __caml_parser_env 0 : 'symbolset) in
    Obj.repr(
# 246 "parse.mly"
      ( let nfa = match !curnfa with 
		 | None ->
		     let newnfa = Nfa.new_nfa_states 0 1 in
		       curnfa := (Some newnfa);
		       newnfa
		 | Some nfa -> nfa
	       in
		 List.iter (fun x -> Nfa.add_trans nfa (to_int_state _1) 
			                               (Nfa.Character x) (to_int_state _3)) _5
	     )
# 660 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 5 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _6 = (Parsing.peek_val __caml_parser_env 0 : 'symbolset) in
    Obj.repr(
# 257 "parse.mly"
      ( let nfa = match !curnfa with 
		 | None ->
		     let newnfa = Nfa.new_nfa_states 0 1 in
		       curnfa := (Some newnfa);
		       newnfa
		 | Some nfa -> nfa
	       in
	       let symbols = Charset.from_list _6 in
	       let symbols = Charset.minus (Charset.create_full ()) symbols in
		 Charset.iter (fun x -> Nfa.add_trans nfa (to_int_state _1) 
				 (Nfa.Character x) (to_int_state _3)) symbols
	     )
# 680 "parse.ml"
               : 'transition))
; (fun __caml_parser_env ->
    Obj.repr(
# 271 "parse.mly"
     ( [] )
# 686 "parse.ml"
               : 'symbolset))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 1 : 'symbollist) in
    Obj.repr(
# 273 "parse.mly"
     ( _2  )
# 693 "parse.ml"
               : 'symbolset))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 2 : 'symbollist) in
    Obj.repr(
# 275 "parse.mly"
     ( _2 )
# 700 "parse.ml"
               : 'symbolset))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'symbol) in
    Obj.repr(
# 278 "parse.mly"
            ( [ _1 ] )
# 707 "parse.ml"
               : 'symbollist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'symbollist) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'symbol) in
    Obj.repr(
# 280 "parse.mly"
            ( _3 :: _1 )
# 715 "parse.ml"
               : 'symbollist))
; (fun __caml_parser_env ->
    Obj.repr(
# 283 "parse.mly"
     ( output_error 1 1 ("Unrecognized symbol; expecting a single-quote" ^
                                " delimited symbol or a decimal character code")
	    )
# 723 "parse.ml"
               : 'symbol))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 287 "parse.mly"
     ( 
	      try Charset.string_to_int _1 with Charset.IllegalChar -> 
		output_error 1 1 "Unrecognized symbol; expected a single character"
            )
# 733 "parse.ml"
               : 'symbol))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 292 "parse.mly"
     ( try Charset.digit_list_to_int _2 with Charset.IllegalChar ->
                output_error 1 2 ("Unrecognized symbol; expected a decimal character" ^
                                  " code of the form \nnn")
            )
# 743 "parse.ml"
               : 'symbol))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'paramoption) in
    Obj.repr(
# 298 "parse.mly"
     ( output_error 1 1 "Unrecognized command" )
# 751 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 300 "parse.mly"
     ( PUSH )
# 757 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 302 "parse.mly"
     ( POP )
# 763 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 304 "parse.mly"
     ( SOLVE )
# 769 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 306 "parse.mly"
     ( SOLVEALL )
# 775 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 308 "parse.mly"
     ( RESET )
# 781 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : int) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : string) in
    Obj.repr(
# 310 "parse.mly"
              (      
		let index = 
		try int_of_string _3 with _ -> 
		  output_error 1 4 "Select takes an integer argument" in
		SELECT index
	      )
# 794 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : string list) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'identlist) in
    Obj.repr(
# 316 "parse.mly"
                                      ( MACHINES _3 )
# 802 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 3 : int * (string list)) in
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'identlist) in
    Obj.repr(
# 317 "parse.mly"
                                     ( STRINGS (1, _3) )
# 810 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 5 : int * (string list)) in
    let _3 = (Parsing.peek_val __caml_parser_env 3 : string) in
    let _5 = (Parsing.peek_val __caml_parser_env 1 : 'identlist) in
    Obj.repr(
# 318 "parse.mly"
                                                 ( 
	      let index = try int_of_string _3 with _ ->
		output_error 3 3 "Invalid number of strings specified" 
	      in
		if index <= 0 then 
		  output_error 3 3 "Invalid number of strings specified" ;
		STRINGS (index, _5) 
            )
# 826 "parse.ml"
               : 'command))
; (fun __caml_parser_env ->
    Obj.repr(
# 327 "parse.mly"
           ( [] )
# 832 "parse.ml"
               : 'identlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'nonemptyidentlist) in
    Obj.repr(
# 328 "parse.mly"
                       ( _1 )
# 839 "parse.ml"
               : 'identlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 331 "parse.mly"
           ( [_1])
# 846 "parse.ml"
               : 'nonemptyidentlist))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'identlist) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 332 "parse.mly"
                                  ( _1 @ [_3] )
# 854 "parse.ml"
               : 'nonemptyidentlist))
; (fun __caml_parser_env ->
    Obj.repr(
# 334 "parse.mly"
                          ( )
# 860 "parse.ml"
               : 'paramoption))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : string) in
    Obj.repr(
# 335 "parse.mly"
             ()
# 867 "parse.ml"
               : 'paramoption))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'identlist) in
    Obj.repr(
# 336 "parse.mly"
                       ( )
# 874 "parse.ml"
               : 'paramoption))
; (fun __caml_parser_env ->
    Obj.repr(
# 337 "parse.mly"
             ( )
# 880 "parse.ml"
               : 'paramoption))
(* Entry statement *)
; (fun __caml_parser_env -> raise (Parsing.YYexit (Parsing.peek_val __caml_parser_env 0)))
|]
let yytables =
  { Parsing.actions=yyact;
    Parsing.transl_const=yytransl_const;
    Parsing.transl_block=yytransl_block;
    Parsing.lhs=yylhs;
    Parsing.len=yylen;
    Parsing.defred=yydefred;
    Parsing.dgoto=yydgoto;
    Parsing.sindex=yysindex;
    Parsing.rindex=yyrindex;
    Parsing.gindex=yygindex;
    Parsing.tablesize=yytablesize;
    Parsing.table=yytable;
    Parsing.check=yycheck;
    Parsing.error_function=parse_error;
    Parsing.names_const=yynames_const;
    Parsing.names_block=yynames_block }
let statement (lexfun : Lexing.lexbuf -> token) (lexbuf : Lexing.lexbuf) =
   (Parsing.yyparse yytables 1 lexfun lexbuf : unit)
