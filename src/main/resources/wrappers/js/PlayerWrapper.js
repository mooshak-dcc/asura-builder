var IOProcessor = require('./IOProcessor');

var ioProcessor = new IOProcessor();

/**
 * Wrapper for players in JavaScript.
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
var PlayerWrapper = function () {
    this.player_id = undefined;
    this.action = {'command': {}, 'messages': []};
};


/********************************************************************************************
 *                         Methods implemented by concrete players                          *
 ********************************************************************************************/

/**
 * Get the name of this player from the concrete player
 *
 * @return {string} the name of this player
 */
PlayerWrapper.prototype.getName = function () {
    return '';
};

/**
 * Provide the concrete player with an opportunity to perform
 * initializations
 */
PlayerWrapper.prototype.init = function () {
};

/**
 * Invoked on the concrete player to ask for his action
 */
PlayerWrapper.prototype.execute = function () {
};


/********************************************************************************************
 *                         Methods implemented by abstract players                          *
 ********************************************************************************************/

/**
 * Update state on abstract players
 *
 * @param state_update {object} the state update
 */
PlayerWrapper.prototype.update = function (state_update) {
};

/**
 * Invoked on the abstract player to manage player lifecycle
 */
PlayerWrapper.prototype.run = function () {
};


/********************************************************************************************
 *                                     General Helpers                                      *
 ********************************************************************************************/

/**
 * Set the player ID
 *
 * @param player_id {string} ID of the player
 */
PlayerWrapper.prototype.setPlayerId = function (player_id) {
    this.player_id = player_id;
};

/**
 * Get the player ID
 *
 * @return {string} ID of the player
 */
PlayerWrapper.prototype.getPlayerId = function () {
    return this.player_id;
};

/**
 * Read a line of input and update the the state of the player with a state update.
 * It expects that the line contains the JSON string representation of a
 * state update.
 */
PlayerWrapper.prototype.readAndUpdate = function () {

    var state_update = JSON.parse(ioProcessor.readline());
    this.update(state_update);
};

/**
 * Send the current action (command and log messages) to the Game Manager
 */
PlayerWrapper.prototype.sendAction = function () {

    var actionToSend = this.action;

    this.action = {'command': {}, 'messages': []};

    ioProcessor.writeln(JSON.stringify(actionToSend));
};

/**
 * Do an action in the game with given name and arguments
 *
 * @param name {string} Name of the command
 * @param args {Array}  Arguments of the command
 */
PlayerWrapper.prototype.doAction = function (name, args) {
    this.action.command = {'name': name, 'args': args};
};

/**
 * Log debugging message
 *
 * @param message debugging message
 */
PlayerWrapper.prototype.log = function (message) {
    this.action.messages.append(message);
};

/**
 * Send the name to the Game Manager
 */
PlayerWrapper.prototype.sendName = function () {
    var name = this.getName();
    this.doAction("NAME", [!name ? this.player_id : name]);
    this.sendAction();
};

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

var program_path = process.argv[2];

// initialize player
var Player = require(program_path);

var player = new Player();

player.setPlayerId(process.argv[3]);

// main
player.init();
player.sendName();
player.run();
