compile_options = -XDignore.symbol.file
# Dirs setup
client_dir = client/
server_dir = server/
src_dir = src/
binaries_dir = bin/
# Cp setup
lib_path = lib/
class_path = $(lib_path)*:.

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
# Client
$(binaries_dir)$(client_dir)*.class: $(src_dir)$(client_dir)*.java
	javac -cp $(class_path) $(src_dir)$(client_dir)*.java -d $(binaries_dir)$(client_dir) $(compile_options)

# Server
$(binaries_dir)$(server_dir)*.class: $(src_dir)$(server_dir)*.java
	javac -cp $(class_path) $(src_dir)$(server_dir)*.java -d $(binaries_dir)$(server_dir) $(compile_options)
