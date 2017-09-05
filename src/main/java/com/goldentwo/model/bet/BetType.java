package com.goldentwo.model.bet;

public enum BetType {
    ONE {
        @Override
        public String toString() {
            return "First team win";
        }
    },

    TWO {
        @Override
        public String toString() {
            return "Second team win";
        }
    },

    ZERO {
        @Override
        public String toString() {
            return "Draw";
        }
    }
}
