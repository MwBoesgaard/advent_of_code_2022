fun main() {

    class Folder(val name: String, var parent: Folder?, val children: MutableList<Folder>, var size: Long) {
        fun getTotalSize(): Long {
            var totalSize: Long = this.size
            for (child in children) {
                totalSize += child.getTotalSize()
            }
            return totalSize
        }
    }

    class FileSystem {
        val root: Folder = Folder("/", null, mutableListOf(), 0)
        val setOfAllFolders: HashSet<Folder> = hashSetOf()
        var currentDirectory: Folder
        val totalDiskSpace = 70_000_000

        init {
            setOfAllFolders.add(root)
            currentDirectory = root
        }

        fun getCurrentDiskSpaceAvailable(): Long {
            return totalDiskSpace - root.getTotalSize()
        }

        fun generateFileSystemFromInput(input: List<String>) {
            for (line in input) {
                val splitLine = line.split(" ")

                if (splitLine[0] == "$" && splitLine[1] == "cd") {
                    currentDirectory = changeDirectory(splitLine[2], currentDirectory, root)
                    setOfAllFolders.add(currentDirectory)
                } else if (splitLine[0] != "$") {
                    if (splitLine[0] == "dir") {
                        currentDirectory.children.add(Folder(splitLine[1], currentDirectory, mutableListOf(), 0))
                    } else {
                        currentDirectory.size += splitLine[0].toInt()
                    }
                }
            }
        }

        fun changeDirectory(arg: String, currentDirectory: Folder, root: Folder): Folder {
            return when (arg) {
                "/" -> root
                ".." -> currentDirectory.parent ?: root
                else -> currentDirectory.children.find { it.name == arg }!!
            }
        }
    }

    fun part1(input: List<String>): Long {
        val fileSystem = FileSystem()
        fileSystem.generateFileSystemFromInput(input)

        return fileSystem.setOfAllFolders
            .map { it.getTotalSize() }
            .filter { it <= 100000 }
            .sum()
    }

    fun part2(input: List<String>): Long {
        val fileSystem = FileSystem()
        fileSystem.generateFileSystemFromInput(input)

        return fileSystem.setOfAllFolders
            .toList()
            .map { it.getTotalSize() }
            .filter { fileSystem.getCurrentDiskSpaceAvailable() + it >= 30_000_000 }
            .min()
    }

    printSolutionFromInputLines("Day07", ::part1)
    printSolutionFromInputLines("Day07", ::part2)
}
