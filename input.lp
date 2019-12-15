%{door(D):wall(D)}.
%1 = {open_door(D);locked_door(D);bomb_door(D)} :- door(D).
%{in_room(I):item(I)}.
% {monster_present(ENEMY_TYPE,COUNT): monster(ENEMY_TYPE), monster_count(COUNT)}.
% {boss_present(BOSS_TYPE): boss(BOSS_TYPE)}.

% some sample input
in_room(key).
monster_present(monster1,4).
door(n,locked).
door(s,open).
% boss_present(level).