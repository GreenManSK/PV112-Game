#version 450 core

out vec4 FragColor;

layout(location = 0) in vec3 fs_position;
layout(location = 1) in vec2 fs_texture_coordinate;

layout(binding = 0) uniform sampler2D texture_image;

void main()
{
    FragColor = texture(texture_image, fs_texture_coordinate);
}