package jaime.ge.gameEvent.games._model.enums;

import jaime.ge.gameEvent.games._model.GamePlayer;

public enum RewardType {
    WIN {
        @Override
        public boolean appliesTo(GamePlayer player, GamePlayer winner) {
            return winner != null && player.getPlayer().getName().equals(winner.getPlayer().getName());
        }
    },
    LOSE {
        @Override
        public boolean appliesTo(GamePlayer player, GamePlayer winner) {
            // Si no hay ganador, todos reciben la recompensa LOSE
            return winner == null || !player.getPlayer().getName().equals(winner.getPlayer().getName());
        }
    },
    PLAY {
        @Override
        public boolean appliesTo(GamePlayer player, GamePlayer winner) {
            return true;
        }
    };

    public abstract boolean appliesTo(GamePlayer player, GamePlayer winner);
}


