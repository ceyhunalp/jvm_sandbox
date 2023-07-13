package contract;

import jnr.ffi.Pointer;
import org.apache.tuweni.crypto.sodium.Sodium;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Write {

    public static int MAX_EMBED_SZ = 20;
    public static int WRITE_POLICY_SZ = 32;
    public static int KEY_SIZE = 20;
    public static int ED25519_CORE_BYTES = 32;
    public static int ED25519_SCALAR_BYTES = 32;
    public static int ED25519_NONREDUCED_SCALAR_BYTES = 64;
    public static int SEEDBYTES = 32;
    public static int SHA256_BYTES = 32;

    public byte[] U;
    public byte[] Ubar;
    public byte[] C;

    public byte[] e;
    public byte[] f;

    public byte[] ltsID;
    public byte[] policy;

    public Write(int seed) {
        U = new byte[ED25519_CORE_BYTES];
        Ubar = new byte[ED25519_CORE_BYTES];
        C = new byte[ED25519_CORE_BYTES];
        e = new byte[SHA256_BYTES];
        f = new byte[ED25519_SCALAR_BYTES];
        ltsID = new byte[SEEDBYTES];
        policy = new byte[SEEDBYTES];

        byte[] seedBytes = ByteBuffer.allocate(SEEDBYTES).putInt(seed).array();
        Sodium.randombytes_buf_deterministic(ltsID, SEEDBYTES, seedBytes);
        Sodium.randombytes_buf_deterministic(policy, SEEDBYTES, seedBytes);
    }

    public static int embedData(byte[] data, int n, byte[] buf, byte[] seed) {
        int dl = Write.MAX_EMBED_SZ;
        if (dl > n) {
            dl = n;
        }

        byte[] randomBuf = new byte[1024 * Write.ED25519_CORE_BYTES];
        Sodium.randombytes_buf_deterministic(randomBuf,
                1024L * Write.ED25519_CORE_BYTES, seed);

        int i = 0;
        while (i < 1023) {
            Arrays.fill(buf, (byte) 0);
            if (Write.ED25519_CORE_BYTES >= 0) {
                System.arraycopy(randomBuf, i, buf, 0, Write.ED25519_CORE_BYTES);
                i += Write.ED25519_CORE_BYTES;
            }
            if (n > 0) {
                buf[0] = (byte) (dl);
                if (dl >= 0) System.arraycopy(data, 0, buf, 1, dl);
                if (Sodium.crypto_core_ed25519_is_valid_point(buf) == 1) {
                    return 0;
                }
            }
        }
        return -1;
    }

    public static void printHex(byte[] buf) {
        for (byte b :
                buf) {
            System.out.printf("%02X", b);
        }
        System.out.println();
    }

    public int newWrite(byte[] X, byte[] key, int keyLen, byte[] seed) {
        byte[] r = new byte[ED25519_SCALAR_BYTES];
        Sodium.crypto_core_ed25519_scalar_random(r);

        if (Sodium.crypto_scalarmult_ed25519_noclamp(C, r, X) != 0) {
            return -1;
        }
        if (Sodium.crypto_scalarmult_ed25519_base_noclamp(U, r) != 0) {
            return -1;
        }
        if (keyLen > MAX_EMBED_SZ) {
            return -1;
        }

        byte[] kp = new byte[ED25519_CORE_BYTES];
        if (embedData(key, keyLen, kp, seed) != 0) {
            return -1;
        }

        if (Sodium.crypto_core_ed25519_add(C, C, kp) != 0) {
            return -1;
        }

        byte[] Gbar = new byte[ED25519_CORE_BYTES];
        if (embedData(ltsID, SEEDBYTES, Gbar, ltsID) != 0) {
            return -1;
        }

        if (Sodium.crypto_scalarmult_ed25519_noclamp(Ubar, r, Gbar) != 0) {
            return -1;
        }

        byte[] s = new byte[ED25519_SCALAR_BYTES];
        byte[] W = new byte[ED25519_CORE_BYTES];
        byte[] Wbar = new byte[ED25519_CORE_BYTES];
        Sodium.crypto_core_ed25519_scalar_random(s);
        if (Sodium.crypto_scalarmult_ed25519_base_noclamp(W, s) != 0) {
            return -1;
        }
        if (Sodium.crypto_scalarmult_ed25519_noclamp(Wbar, s, Gbar) != 0) {
            return -1;
        }

        Pointer p = Sodium.malloc(104);
        if (Sodium.crypto_hash_sha256_init(p) != 0) {
            return -1;
        }
        Sodium.crypto_hash_sha256_update(p, C, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, U, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, Ubar, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, W, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, Wbar, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, policy, WRITE_POLICY_SZ);

        byte[] hash = new byte[ED25519_NONREDUCED_SCALAR_BYTES];
        Sodium.crypto_hash_sha256_final(p, hash);
        for (int i = ED25519_SCALAR_BYTES; i < ED25519_NONREDUCED_SCALAR_BYTES; i++) {
            hash[i] = (byte) 0;
        }
        Sodium.crypto_core_ed25519_scalar_reduce(e, hash);

        byte[] re = new byte[ED25519_SCALAR_BYTES];
        Sodium.crypto_core_ed25519_scalar_mul(re, r, e);
        Sodium.crypto_core_ed25519_scalar_add(f, s, re);
        Sodium.sodium_free(p);
        return 0;
    }

    public int checkProof() {
        byte[] Gf = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_scalarmult_ed25519_base_noclamp(Gf, this.f) != 0) {
            System.err.println("Gf error");
            return -1;
        }
        byte[] Ue = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_scalarmult_ed25519_noclamp(Ue, this.e, this.U) != 0) {
            System.err.println("Ue error");
            return -1;
        }
        byte[] W = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_core_ed25519_sub(W, Gf, Ue) != 0) {
            System.err.println("W error");
            return -1;
        }
        byte[] Gbar = new byte[ED25519_CORE_BYTES];
        if (embedData(this.ltsID, SEEDBYTES, Gbar, this.ltsID) != 0) {
            System.err.println("Gbar error");
            return -1;
        }
        byte[] fGbar = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_scalarmult_ed25519_noclamp(fGbar, this.f, Gbar) != 0) {
            System.err.println("fGbar error");
            return -1;
        }
        byte[] UeBar = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_scalarmult_ed25519_noclamp(UeBar, this.e, this.Ubar) != 0) {
            System.err.println("UeBar error");
            return -1;
        }
        byte[] Wbar = new byte[ED25519_CORE_BYTES];
        if (Sodium.crypto_core_ed25519_sub(Wbar, fGbar, UeBar) != 0) {
            System.err.println("Wbar error");
            return -1;
        }

        Pointer p = Sodium.malloc(104);
        if (Sodium.crypto_hash_sha256_init(p) != 0) {
            return -1;
        }
        Sodium.crypto_hash_sha256_update(p, this.C, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, this.U, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, this.Ubar, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, W, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, Wbar, ED25519_CORE_BYTES);
        Sodium.crypto_hash_sha256_update(p, this.policy, WRITE_POLICY_SZ);

        byte[] hash = new byte[ED25519_NONREDUCED_SCALAR_BYTES];
        Sodium.crypto_hash_sha256_final(p, hash);
        for (int i = ED25519_SCALAR_BYTES; i < ED25519_NONREDUCED_SCALAR_BYTES; i++) {
            hash[i] = (byte) 0;
        }

        byte[] e = new byte[ED25519_SCALAR_BYTES];
        Sodium.crypto_core_ed25519_scalar_reduce(e, hash);
        Sodium.sodium_free(p);
        if (Arrays.equals(e, this.e)) return 0;
        return -1;
    }
}