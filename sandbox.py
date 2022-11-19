import sys
import os
import argparse
import tomli_w

def generate_toml(rootdir, src_files, wl_class, wl_method, bl_method, outfile):
    d = dict()
    wl_class = os.path.abspath(wl_class)
    wl_method = os.path.abspath(wl_method)
    bl_method = os.path.abspath(bl_method)
    d["sources"] = {"root_dir": rootdir, "src_files": src_files}
    d["whitelist"] = {"class_file": wl_class, "method_file": wl_method}
    d["blacklist"] = {"method_file": bl_method}
    with open(outfile, "wb") as f:
        tomli_w.dump(d, f)
    f.close()

def run(args):
    src_files = []
    rootdir = os.path.abspath(args.rootdir)
    for dpath, dirs, files in os.walk(rootdir):
        for f in files:
            if f.endswith(".java"):
                src_files.append(os.path.join(dpath, f))
    generate_toml(rootdir, src_files, args.wclass, args.wmethod, args.bmethod, args.output)

def main(args):
    ret = run(args)
    return ret

if __name__ == '__main__':
    try:
        parser = argparse.ArgumentParser(description='Execute program in the deterministic JVM sandbox')
        parser.add_argument('-d', '--rootdir', type=str, required=True,
                            help='top level directory path for Java source files')
        parser.add_argument('-wc', '--wclass', type=str, required=True,
                            help='file with whitelisted class names')
        parser.add_argument('-wm', '--wmethod', type=str, required=True,
                            help='file with whitelisted method names')
        parser.add_argument('-b', '--bmethod', type=str, required=True,
                            help='file with blacklisted method names')
        parser.add_argument('-o', '--output', type=str, required=True,
                            help='TOML config file name')
        args = parser.parse_args()
        sys.exit(main(args))
    except KeyboardInterrupt:
        logger.warning('KeyboardInterrupt')
        sys.exit(1)
