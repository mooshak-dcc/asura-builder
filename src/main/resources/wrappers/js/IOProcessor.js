/******************************************************************************
 *                            Input/Output Processor                          *
 ******************************************************************************/

var fs = require('fs');

/**
 * Processor for dealing with Input/Output
 */
var IOProcessor = function () {
    this.input_text = '';
};

/**
 * Read input from stdin until end_byte is read.
 *
 * @param end_byte Byte that delimits strings
 * @returns {string} String until end_byte
 */
IOProcessor.prototype.readUntilByte = function (end_byte) {
    var buf_size = 1024;
    var buf = Buffer.alloc(buf_size);
    var total_buf = Buffer.alloc(buf_size);
    var total_bytes_read = 0;
    var bytes_read = 0;
    var end_byte_read = false;

    var fd = process.stdin.fd;

    var using_device = false;
    try {
        fd = fs.openSync('/dev/stdin', 'rs');
        using_device = true;
    } catch (e) {
    }

    for (; ;) {
        try {
            bytes_read = fs.readSync(fd, buf, 0, buf_size, null);

            // Copy the new bytes to total_buf.
            var tmp_buf = Buffer.alloc(total_bytes_read + bytes_read);
            total_buf.copy(tmp_buf, 0, 0, total_bytes_read);
            buf.copy(tmp_buf, total_bytes_read, 0, bytes_read);
            total_buf = tmp_buf;
            total_bytes_read += bytes_read;

            // Has the end_byte been read?
            for (var i = 0; i < bytes_read; i++) {
                if (buf[i] === end_byte) {
                    end_byte_read = true;
                    break;
                }
            }
            if (end_byte_read) {
                break;
            }
        } catch (e) {
            if (e.code === 'EOF') {
                break;
            }
            throw e;
        }
        if (bytes_read === 0) {
            break;
        }
    }

    if (using_device) {
        fs.closeSync(fd);
    }

    return total_buf.toString();
};

/**
 * Read a line from standard input
 */
IOProcessor.prototype.readline = function () {

    if (this.input_text.length === 0)
        this.input_text = this.readUntilByte('\n'.charCodeAt(0));

    var newline = this.input_text.search('\n') + 1;
    var line = this.input_text.slice(0, newline);

    this.input_text = this.input_text.slice(newline);

    return line;
};

/**
 * Write a line with a message to the standard output
 *
 * @param message {string} the message to write to the standard output
 */
IOProcessor.prototype.writeln = function (message) {
    process.stdout.write(message);
    process.stdout.write('\n');
};

// export input/output processor
module.exports = IOProcessor;
