const IOProcessor = require('./IOProcessor');

const ioProcessor = new IOProcessor();

/**
 * Wrapper for players in JavaScript.
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
class PlayerWrapper {

    constructor() {
        this.player_id = undefined;
        this.action = {'command': {}, 'messages': []};
    }

    /********************************************************************************************
     *                         Methods implemented by concrete players                          *
     ********************************************************************************************/

    /**
     * Get the name of this player from the concrete player
     *
     * @returns {string} the name of this player
     */
    getName() {
        return '';
    }

    /**
     * Provide the concrete player with an opportunity to perform
     * initializations
     */
    init() {
    }

    /**
     * Invoked on the concrete player to ask for his action
     */
    execute() {
    }

    /********************************************************************************************
     *                         Methods implemented by abstract players                          *
     ********************************************************************************************/

    /**
     * Update state on abstract players
     *
     * @param state_update {object} the state update
     */
    update(state_update) {
    }

    /**
     * Invoked on the abstract player to manage player lifecycle
     */
    run() {
    }

    /********************************************************************************************
     *                                     General Helpers                                      *
     ********************************************************************************************/

    /**
     * Set the player ID
     *
     * @param player_id {string} ID of the player
     */
    setPlayerId(player_id) {
        this.player_id = player_id;
    }

    /**
     * Get the player ID
     *
     * @returns {string} ID of the player
     */
    getPlayerId() {
        return this.player_id;
    }

    /**
     * Read a line of input and update the the state of the player with a state update.
     * It expects that the line contains the JSON string representation of a
     * state update.
     */
    readAndUpdate() {

        let state_update = JSON.parse(ioProcessor.readline());
        this.update(state_update);
    }

    /**
     * Send the current action (command and log messages) to the Game Manager
     */
    sendAction() {

        let actionToSend = this.action;

        this.action = {'command': {}, 'messages': []};

        ioProcessor.writeln(JSON.stringify(actionToSend));
    }

    /**
     * Do an action in the game with given name and arguments
     *
     * @param name {string} Name of the command
     * @param args {Array}  Arguments of the command
     */
    doAction(name, args) {
        this.action.command = {'name': name, 'args': args};
    }

    /**
     * Log debugging message
     *
     * @param message debugging message
     */
    log(message) {
        this.action.messages.append(message);
    }

    /**
     * Send the name to the Game Manager
     */
    sendName() {
        let name = this.getName();
        this.doAction("NAME", [!name ? this.player_id : name]);
        this.sendAction();
    }
}

// export player wrapper
module.exports = PlayerWrapper;



/********************************************************************************************
 *                                       Main                                               *
 ********************************************************************************************/

// parsing arguments
if (process.argv.length !== 4) {
    process.stderr.write('usage: {path/to/player/code} {player-id}');
    process.stderr.write('\n');
    process.exit(-1);
}

let program_path = process.argv[2];

// initialize player
const Player = require(program_path);

let player = new Player();

player.setPlayerId(process.argv[3]);

// main
player.init();
player.sendName();
player.run();

