#version 450 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture_coordinate;

layout(location = 0) out vec3 fs_position;
layout(location = 1) out vec2 fs_texture_coordinate;

void main()
{
    fs_position = position;
    fs_texture_coordinate = texture_coordinate;

    gl_Position = vec4(fs_position, 1.0);
}