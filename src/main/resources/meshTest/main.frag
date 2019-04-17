#version 330 core
#extension GL_ARB_separate_shader_objects : enable

out vec4 FragColor;

//layout(binding = 0) uniform sampler2D texture_image;

layout(location = 0) in vec3 fs_position;
layout(location = 1) in vec3 fs_normal;
layout(location = 2) in vec2 fs_texture_coordinate;

layout(location = 0) out vec4 final_color;

void main()
{
    //FragColor = texture(texture_image, fs_texture_coordinate);
    FragColor = vec4(1.0, 0.0, 1.0, 1.0);
}