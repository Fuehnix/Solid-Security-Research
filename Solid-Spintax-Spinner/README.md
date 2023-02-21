[![Solid Security](https://www.solidsecurity.co/wp-content/uploads/2020/03/logo-18-7.png "Solid Security")](https://solidsecurity.co "Solid Security")
# Solid Spintax Spinner
![](https://img.shields.io/github/tag/SolidSecurity/Solid-Spintax-Spinner.svg) ![](https://img.shields.io/github/release/SolidSecurity/Solid-Spintax-Spinner.svg) ![](https://img.shields.io/github/issues/SolidSecurity/Solid-Spintax-Spinner.svg)

The Solid Spintax Spintaxer is a command-line utility to generate uniquely identifiable documents using a provided spintax. Spintaxes are built according to the [Solid Spintax standard](https://github.com/SolidSecurity/Solid-Spintax-Specification), which is backwards-compatible with standard spintax.

Unlike traditional pseudorandom implementations, this spintaxer is completely deterministic &mdash; every possible permutation of a document is given a unique identifier, which can later be recovered using the [Solid Spintax Investigator](https://github.com/SolidSecurity/Solid-Spintax-Investigator).

## Installation
### Bash Script
If you are just looking to get up and running with the spintaxer as quickly as possible, you can run these two commands to download the latest Solid Spintaxer and create an alias for you automatically:

```
git clone https://github.com/SolidSecurity/Solid-Spintax-Spinner.git
echo "alias spinner='java -jar $PWD/Solid-Spintax-Spinner/dist/Solid-Spintax-Spinner.jar'" >> ~/.bashrc
```

### Download
#### (Recommended) Download Release
Download the latest release as a .zip containing required dependencies along with an executable .jar file from our [release page](https://github.com/SolidSecurity/Solid-Spintax-Spinner/releases).

#### (Alternative) Build from Source
The repository also contains NetBeans project files for those who wish to make modifications and then build the repository themselves.

### Running
#### Command Line
You can run dist/SolidSpintaxSpinner.jar directly from the command line with the following comand (after cloning the repository into the current directory):

`java -jar Solid-Spintax-Spinner/dist/Solid-Spintax-Spinner.jar <input.sstx> [-arguments]`

#### (Optional) Create Alias
Optionally, create an alias for the installed command like so:

`echo "alias spinner='java -jar $PWD/Solid-Spintax-Spinner/dist/Solid-Spintax-Spinner.jar'" >> ~/.bashrc`

The spinner can then be run directly using:

`spinner <input.sstx> [-arguments]`

## Help
Use the `-h` or `--help` to get a shortened version of the help information below:

### Usage
The spinner accepts an input file and a set of arguments:

`spinner [-r] [-s] [-a] [-u] [-p <#>] [-t <TAG>] [-n <#>] [-y] [-o <FILE>] [-l <FILE>] [-i] [-v] [-h] [<FILE>]`

### Options
#### Modes
 Specify how the spinner should choose which permutations to generate:

`-r, --random`: generate random permutations of the provided spintax

For each output file, random will pick a random permutation in [0, permutations) and output that permutation. Therefore, the output is uniformly random in tag space (but not necessarily uniform switch by switch). Outputs are not necessarily unique unless `-u` is specified. By default, only 1 random file is generated unless `-c <#>` is specified.

`-s, --sequential`: generates sequential permutations of the provided spintax

Unlike `-r`, the files generated will be in sequential order (eg. permutation #000, permutation #001, permutation #002...), starting at a provided the provided tag or permutation number if one is provided (eg. permutation #125, permutation #126, permutation #127...).

`-a, --all`: generate all  possible  permutations  of  the  provided spintax

Generates every possible permutation of the Spintax (could be a huge number of files if the input spintax is large). Equivalent to sequential with `--count` equal to number of possible permutations.

#### Restrictions
Add restrictions that the spinner must follow

`-u, --unique`: require all output documents to be unique

By default, `-r` (random) picks permutations randomly and may end up generating the same permutation twice by random chance. `-r -u` or `-ru` (random unique) requires that each output file be different. `--count` must be less than or equal to the number of possible permutations when unique is used.

#### Identifiers
Identifies a particular permutation to be generated

`-p <#>, --permutation <#>`: generate  the  output   document   corresponding  to  a specific permutation number

When used on its own, this will result in the spinner outputting exactly one file corresponding to the provided permutation number. When used with `--sequential` and `--count`, the sequential output will begin at the specified permutation and increment from there.

`-t <TAG>, --tag <TAG>`:  generate  the  output   document   corresponding  to  a specific tag

When used on its own, this will result in the spinner outputting exactly one file corresponding to the provided tag. When used with `--sequential` and `--count`, the sequential output will begin at the specified tag and increment from there. Note that per the Solid Spintax standard, tag is just a base36 encoding of permutation.

#### Output
Specify where output files should be stored

`-n <#>, --count <#>` : specifies the number of files to be generated

When used with `--sequential` or `--random`, this specifies exactly how many files should be generated. If the specified value is greater than or equal to `FILE_WARNING_NUM` (100 by default), a confirmation will be printed asking you to acknowledge generating a large number of files; use `-y` to skip this confirmation. Generating more than `FILE_REJECT_NUM` files (100,000 by default) is not supported; modify the source yourself if you need to enable dangerous operations like this.

`-y, --yes`: automatically acknowledge  generating  large  number of output files

Automatically confirm generating large numbers of files (for when `spinner` is being called from within another script). Danger: you will not be notified before generating a large number of output files.

`-o <FILE>, --out <FILE>`: specifies  the  suffix  to  be  appended  to  generated output files

By default, output files are named the same as your input file but with ".out" appended and a numerical prefix prepended (eg. "input.sstx" becomes "00input.ssstx.out", "01input.sstx.out", "02input.sstx.out"...). Using `--out <suffix>` specifies a file suffix to be used instead of this default behavior (eg. --out "out.txt" becomes "00out.txt", "01out.txt", "02out.txt"...).

`-l <FILE>, --log <FILE>`: creates a  separate  file  with  tags  corresponding to  generated documents

Creates a log file mapping output documents to their corresponding tags in `OUTPUT_FILE:TAG` format. For example, if using `--log file.log`, file.log might look like this after the spinner runs:

```
0a.sstx.out:H5G4
1a.sstx.out:7VKD
2a.sstx.out:45WU
...
```

#### Information
Request information about this program or provided spintax

`-i, --info`: print information about the provided spintax

Specifically, prints the number of switches and number of permutations in the output file, along with the minimum and maximum valid values for permutation and tag. For example, running `spinner input.sstx -i`:

```
Information about provided spintax file:
        Number of Switches: 2
        Number of Permutations: 1000000
        Valid Permutations: [0, 999999]
        Valid Tags: [0, LFLR]
		...
```

`-v, --version`: prints the current spintax and spinner version

`-h, --help`: shows a shortened version of these docs
