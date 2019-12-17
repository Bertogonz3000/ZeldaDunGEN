% 14*9 grid that includes walls.

#const max_width=14.
#const max_height=9.

max_width(max_width).
max_height(max_height).

width(1..max_width).
height(1..max_height).

% DOOR PLACEMENT
door_at(7, 1, TYPE) :- door(n,TYPE), door_type(TYPE).
door_at(1, 5, TYPE) :- door(w,TYPE), door_type(TYPE).
door_at(14, 5, TYPE) :- door(e,TYPE), door_type(TYPE).
door_at(7, 9, TYPE) :- door(s,TYPE), door_type(TYPE).

inside(X,Y) :- width(X), height(Y), X > 1, X < max_width, Y > 1, Y < max_height.

wall_at(X,Y) :- width(X), height(Y), not inside(X,Y), not door_at(X,Y,_).

% ITEM PLACEMENT, 1 per room
1{item_at(3..12,3..7,I): in_room(I)}1 :- in_room(I).
:- item_at(X,_,shard), X != 7.
:- item_at(_,Y,shard), Y != 5.

% don't actually add something, just the entrance
:- item_at(X,_,entrance), X != 7.
:- item_at(_,Y,entrance), Y!= 7.

statue_at(3,3) :- in_room(entrance). 
statue_at(6,3) :- in_room(entrance).
statue_at(9,3) :- in_room(entrance).
statue_at(12,3) :- in_room(entrance).
statue_at(3,5) :- in_room(entrance). 
statue_at(6,5) :- in_room(entrance).
statue_at(9,5) :- in_room(entrance).
statue_at(12,5) :- in_room(entrance).
statue_at(3,7) :- in_room(entrance). 
statue_at(6,7) :- in_room(entrance).
statue_at(9,7) :- in_room(entrance).
statue_at(12,7) :- in_room(entrance).



% MONSTER PLACEMENT
N = {monster_at(XX,YY,E):inside(XX,YY)} :- monster_present(E,N).

% reachability stuff I'll play with if we will end up doing mazes
% reachable(XX,YY) :- reachable(XX+1,YY), not block_at(XX,YY), inside(XX,YY).
% reachable(XX,YY) :- reachable(XX-1,YY), not block_at(XX,YY), inside(XX,YY).
% reachable(XX,YY) :- reachable(XX,YY+1), not block_at(XX,YY), inside(XX,YY).
% reachable(XX,YY) :- reachable(XX,YY-1), not block_at(XX,YY), inside(XX,YY).


% "sixblock" pattern where there is a 2*3 cluster of blocks
block_at(X,Y) :- sixblock(X,Y).
block_at(X,Y+1) :- sixblock(X,Y).
block_at(X,Y+2) :- sixblock(X,Y).
block_at(X+1,Y) :- sixblock(X,Y).
block_at(X+1,Y+1) :- sixblock(X,Y).
block_at(X+1,Y+2) :- sixblock(X,Y).

{sixblock(X,Y):inside(X,Y)}2.

% four-way symmetrical block pattern
block_at(7-X_SEP,5-Y_SEP) :- fourway(X_SEP, Y_SEP).
block_at(8+X_SEP,5-Y_SEP) :- fourway(X_SEP, Y_SEP).
block_at(7-X_SEP,5+Y_SEP) :- fourway(X_SEP, Y_SEP).
block_at(8+X_SEP,5+Y_SEP) :- fourway(X_SEP, Y_SEP).

{fourway(1..5,1..3)}1.

% prevents overlaps
:- block_at(X,Y), monster_at(X,Y,_).
:- block_at(X,Y), item_at(X,Y,_).
:- item_at(X,Y,_), monster_at(X,Y,_).
:- width(X), height(Y), 2{monster_at(X,Y,_)}.

% additional constraints to prevent grouping on top and too many blocks
:- fourway(_,_), sixblock(_,_).
:- monster_at(_,2,_).
:- monster_at(_,3,_).
:- sixblock(_,2).

1{boss_at(3..9,4..6,BOSS_TYPE): boss_present(BOSS_TYPE)}1 :- boss_present(BOSS_TYPE).

% the box-like block placement for the goal room
block_at(3,3) :- in_room(shard).
block_at(4,3) :- in_room(shard).
block_at(5,3) :- in_room(shard).
block_at(6,3) :- in_room(shard).
block_at(7,3) :- in_room(shard).
block_at(8,3) :- in_room(shard).
block_at(9,3) :- in_room(shard).
block_at(10,3) :- in_room(shard).
block_at(11,3) :- in_room(shard).
block_at(12,3) :- in_room(shard).
block_at(3,4) :- in_room(shard).
block_at(3,5) :- in_room(shard).
block_at(3,6) :- in_room(shard).
block_at(3,7) :- in_room(shard).
block_at(12,4) :- in_room(shard).
block_at(12,5) :- in_room(shard).
block_at(12,6) :- in_room(shard).
block_at(12,7) :- in_room(shard).
block_at(4,7) :- in_room(shard).
block_at(5,7) :- in_room(shard).
block_at(6,7) :- in_room(shard).
block_at(9,7) :- in_room(shard).
block_at(10,7) :- in_room(shard).
block_at(11,7) :- in_room(shard).


% keeping the boss and goal rooms empty
:- boss_at(_,_,_), sixblock(_,_).
:- boss_at(_,_,_), fourway(_,_).
:- boss_present(mini), monster_at(_,_,_).
:- boss_present(level), monster_at(_,_,_).
:- in_room(entrance), sixblock(_,_).
:- in_room(entrance), fourway(_,_).
:- in_room(shard), sixblock(_,_).
:- in_room(shard), fourway(_,_).
:- in_room(shard), monster_at(_,_,_).

% making sure nothing blocks doorways
:- door_at(X,Y,_), block_at(X+1,Y).
:- door_at(X,Y,_), block_at(X-1,Y).
:- door_at(X,Y,_), block_at(X,Y+1).
:- door_at(X,Y,_), block_at(X,Y-1).

#show statue_at/2.
#show wall_at/2. 
#show door_at/3.
#show monster_at/3.
#show block_at/2.
#show item_at/3.
#show boss_at/3.