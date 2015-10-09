# Get the directory that this configuration file exists in
dir = File.dirname(__FILE__)

# Load the sencha-touch framework automatically.
load File.join(dir, '..', 'themes')

# Compass configurations
fonts_path = File.join(dir, '..', 'themes/fonts/')
sass_path = dir
css_path = File.join(dir, "..", "css")
environment  = :development
output_style = :compressed
