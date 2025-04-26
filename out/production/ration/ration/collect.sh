#!/bin/zsh

# Define the output file name
output_file="java_codes.rtf"

# Write the RTF header and font table to the output file.
# Note: RTF font sizes are specified in half-points so 16pt = 32 and 12pt = 24.
cat << 'EOF' > "$output_file"
{\rtf1\ansi\deff0
{\fonttbl{\f0 Arial;}}
EOF

# Loop over every .java file in the current directory
for java_file in *.java; do
    if [ -f "$java_file" ]; then
        # Write the header: center aligned (\qc), bold (\b) with 16pt font (\fs32)
        echo "\n\qc\b\fs32 $java_file\b0\par" >> "$output_file"
        
        # Start a new paragraph for code, left aligned (\ql) with 12pt font (\fs24)
        echo "\ql\fs24" >> "$output_file"
        
        # Read the .java file line by line
        while IFS= read -r line; do
            # Escape RTF special characters: \, {, }
            escaped_line=$(echo "$line" | sed -e 's/\\/\\\\/g' -e 's/{/\\{/g' -e 's/}/\\}/g')
            echo "$escaped_line\par" >> "$output_file"
        done < "$java_file"
        
        # Add an extra blank line after each file's content for separation
        echo "\par" >> "$output_file"
    fi
done

# Close the RTF document
echo "}" >> "$output_file"
