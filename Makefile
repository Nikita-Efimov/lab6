compile_options = -XDignore.symbol.file
# Dirs setup
client_dir = client/
server_dir = server/
src_dir = src/
binaries_dir = bin/
structs_dir = structs/
other_dir = other/
# Cp setup
lib_path = lib/
class_path = $(lib_path)*:$(binaries_dir)$(structs_dir):$(binaries_dir)$(other_dir):.

# Startups
# Client
client: $(binaries_dir)$(client_dir)*.class
	######## START #########
	java -cp $(class_path):$(binaries_dir)$(client_dir) Client

# Server
server: $(binaries_dir)$(server_dir)*.class
	######## START #########
	java -cp $(class_path):$(binaries_dir)$(server_dir) Server

# Compiling
# Other
$(binaries_dir)$(other_dir)*.class: $(src_dir)$(other_dir)*.java bin
	mkdir -p $(binaries_dir)$(other_dir)
	javac -cp $(class_path) $(src_dir)$(other_dir)*.java -d $(binaries_dir)$(other_dir) $(compile_options)

# Structs
$(binaries_dir)$(structs_dir)*.class: $(src_dir)$(structs_dir)*.java bin
	mkdir -p $(binaries_dir)$(structs_dir)
	javac -cp $(class_path) $(src_dir)$(structs_dir)*.java -d $(binaries_dir)$(structs_dir) $(compile_options)

# Client
$(binaries_dir)$(client_dir)*.class: $(src_dir)$(client_dir)*.java $(binaries_dir)$(structs_dir)*.class $(binaries_dir)$(other_dir)*.class bin
	mkdir -p $(binaries_dir)$(client_dir)
	javac -cp $(class_path) $(src_dir)$(client_dir)*.java -d $(binaries_dir)$(client_dir) $(compile_options)

# Server
$(binaries_dir)$(server_dir)*.class: $(src_dir)$(server_dir)*.java $(binaries_dir)$(structs_dir)*.class $(binaries_dir)$(other_dir)*.class bin
	mkdir -p $(binaries_dir)$(server_dir)
	javac -cp $(class_path) $(src_dir)$(server_dir)*.java -d $(binaries_dir)$(server_dir) $(compile_options)

bin:
	mkdir -p bin

clear:
	rm -rf bin
