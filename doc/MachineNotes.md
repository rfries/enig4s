## Machine Types

### Enigma-like

A reflector-based rotary machine where each wheel (and other components, such as a plugboard)
transform a character in turn, based on a number of settings. This type of machine includes
most Enigma models, TypeX, and a number of similar machines such as the Swiss NEMA.
They are reciprocal because of the symmetry of the reflector-based design (reversing the encryption
steps yields the same electrical path). This is the only type currently implemented.

### SIGABA-like

A non-reciprocal rotary machine, with no reflector, and an encrypt-decrypt mode selector.  This
type of machine requires a mode switch since the input and output signal must be connected to
the opposite end of the wheel stack when changing from encryption to decryption. This is due to
the asymmetric electrical path.

### Lorenz-like

A reciprocal rotary machine that operates by generating a pseudo-random stream of numbers which
are then combined (via XOR) with the input to produce an encrypted stream (i.e. a Vernam cipher).
