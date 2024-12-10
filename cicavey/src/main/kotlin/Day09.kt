import java.io.File

data class FileRecord (val id: Int, val start: Int, val size: Int, val end: Int = start + size - 1, val range: IntRange = start..end)
data class Free(var start: Int, var size: Int, var range: IntRange = start..start+size - 1) {
    val empty: Boolean get() = size <= 0
    fun consume(amt: Int): Boolean {
        start += amt
        size -= amt
        range = start..start+size-1
        return empty
    }
}

fun main() {
    val diskMap = File("src/main/kotlin/Day09.txt").readText().toCharArray()

    // count up the blocks to allocate an array
    var totalBlocks = diskMap.sumOf { it - '0'}
    val blockMap = IntArray(totalBlocks) { -1 }
    var fileList = mutableListOf<FileRecord>()
    val freeList = mutableListOf<Free>()
    var fileId = -1
    var blockIdx = 0
    for(i in diskMap.indices) {
        val numBlocks = diskMap[i] - '0'
        if(i % 2 == 0) {
            fileId++
            fileList.add(FileRecord(fileId, blockIdx, numBlocks))
            for(k in 0..<numBlocks) {
                blockMap[blockIdx++] = fileId
            }
        } else {
            freeList.add(Free(blockIdx, numBlocks))
            blockIdx += numBlocks
        }
    }

    fileList.sortByDescending(FileRecord::id)

    val blockDefrag = blockMap.copyOf()

    var nextFreeIdx = 0
    var nextBlockIdx = blockDefrag.size - 1

    while(nextFreeIdx < nextBlockIdx) {
        while (blockDefrag[nextFreeIdx++] != -1) { }

        // edge case
        if(nextFreeIdx > nextBlockIdx) {
            break
        }

        blockDefrag[nextFreeIdx-1] = blockDefrag[nextBlockIdx]
        blockDefrag[nextBlockIdx] = -1

        while(blockDefrag[--nextBlockIdx] == -1) { }
    }

    println(blockDefrag.mapIndexed { index, value -> if(value != -1) (index * value).toLong() else 0L}.sum())

    // Part 2 - move contiguous files
    val fileDefrag = blockMap.copyOf()
    while(fileList.isNotEmpty()) {
        val f = fileList.removeFirst()
        val freeChunk = freeList.firstOrNull { f.size <= it.size }
        if (freeChunk != null && freeChunk.start < f.start) {
            // Update block map now
            f.range.zip(freeChunk.range).forEach { (src, dst) ->
                fileDefrag[src] = -1
                fileDefrag[dst] = f.id
            }
            if(freeChunk.consume(f.size)) {
                freeList.remove(freeChunk)
            }
        }
    }
    println(fileDefrag.mapIndexed { index, value -> if(value != -1) (index * value).toLong() else 0L}.sum())
}