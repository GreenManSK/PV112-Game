#version 450 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 texture_coordinate;

layout(location = 0) out vec3 fs_position;
layout(location = 1) out vec3 fs_normal;
layout(location = 2) out vec2 fs_texture_coordinate;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    fs_position = vec3(model * vec4(position, 1.0));
    fs_normal = mat3(transpose(inverse(model))) * normal;
    fs_texture_coordinate = position.xy;

    gl_Position = projection * view * vec4(fs_position, 1.0);
}