## Machine Types

- Reflector-based character machines (where each wheel transforms an entire
  character); includes Enigma, TypeX, and similar machines.  These are reciprocal
  because of the symmetry of the reflector-based design (reversing the encryption
  steps yields the same electrical path) This is the only type currently implemented.

- Non-reciprocal character machines (no reflector, encrypt-decrypt modes).  This type
  of machine requires a mode switch since the keyboard/reader (and printer) must be
  connected to different wheels (i.e. leftmost or rightmost) for encryption and decryption.
  This is due to the electrical path being asymmetric.

- Reciprocal bit-wise machines (where each wheel transforms one bit of a character)
  such as the Lorenz. These are reciprocal because they are based on a Vernam/XOR
  cipher, so adding and subtracting the key stream are electrically the same operation.

